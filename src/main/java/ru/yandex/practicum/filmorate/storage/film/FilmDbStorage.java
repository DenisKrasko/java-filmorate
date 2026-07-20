package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Primary
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {
	private static final String FIND_ALL_QUERY =
					"SELECT f.id, f.name, f.description, f.release_date, f.duration, " +
					"f.ratingid, m.rating " +
					"FROM film f " +
					"LEFT JOIN mpa m ON f.ratingid = m.id";

	private static final String FIND_BY_ID_QUERY =
			"SELECT f.id, f.name, f.description, f.release_date, f.duration, " +
					"f.ratingid, m.rating " +
					"FROM film f " +
					"LEFT JOIN mpa m ON f.ratingid = m.id " +
					"WHERE f.id = ?";

	private static final String FIND_POPULAR_QUERY =
			"SELECT f.id, f.name, f.description, f.release_date, f.duration, " +
					"f.ratingid, m.rating, " +
					"COUNT(l.user_id) AS likes_count " +
					"FROM film f " +
					"LEFT JOIN mpa m ON f.ratingid = m.id " +
					"LEFT JOIN storage_user_film_like l ON f.id = l.film_id " + // Подключаем таблицу лайков (сверьте имя таблицы в вашей schema.sql!)
					"GROUP BY f.id, m.rating " +
					"ORDER BY likes_count DESC " +
					"LIMIT ?";



	private static final String FIND_GENRES_BY_FILM_ID = "SELECT g.id, g.name FROM genre g " + // Сверяем имя таблицы genre из schema.sql
			"JOIN film_genre fg ON g.id = fg.genre_id " +
			"WHERE fg.film_id = ? " +
			"ORDER BY g.id";




	private static final String FIND_FILM_BY_ID_QUERY = """
			SELECT f.id, f.name, f.description, f.release_date, f.duration, f.ratingid,
			       m.rating 
			FROM film f 
			LEFT JOIN mpa m ON f.ratingid = m.id 
			WHERE f.id = ?
			""";
	private static final String CREATE_FILM_QUERY = "";

	//	private static final String FIND_BY_ID_QUERY = "SELECT * FROM film WHERE id = ?";
	private static final String FIND_BY_NAME_QUERY = "SELECT * FROM film WHERE name = ?";
//	private static final String FIND_GENRES_BY_FILM_ID =
//					"SELECT g.id, g.name FROM genre g " +
//					"JOIN film_genres fg ON g.id = fg.genre_id " +
//					"WHERE fg.film_id = ? " +
//					"ORDER BY g.id";

	private static final String INSERT_QUERY = """
			INSERT INTO film (name, description, duration, release_date, ratingid) 
			VALUES (?, ?, ?, ?, ?)
			""";
	public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
		super(jdbc, mapper);
	}

	@Override
	public void addLike(Long filmId, Long userId) {
		// Используем INSERT IGNORE или просто обычный INSERT.
		// Если в схеме стоит составной первичный ключ (film_id, user_id), дубликатов не будет.
		String sql = "INSERT INTO storage_user_film_like (film_id, user_id) VALUES (?, ?)";
		jdbc.update(sql, filmId, userId);
	}

	@Override
	public void removeLike(Long filmId, Long userId) {
		String sql = "DELETE FROM storage_user_film_like WHERE film_id = ? AND user_id = ?";
		int rowsAffected = jdbc.update(sql, filmId, userId);

		// Если тест требует падать с 404, если лайка не было, можно проверить rowsAffected:
		if (rowsAffected == 0) {
			throw new NotFoundException("Лайк от пользователя " + userId + " не найден");
		}
	}

	@Override
	public Film save( Film film) {
		// Безопасно достаем id рейтинга, если mpa существует
		Long mpaId = (film.getMpa() != null) ? film.getMpa().getId() : null;

		long id = insert(
				INSERT_QUERY,
				film.getName(),
				film.getDescription(),
				film.getDuration(),
				film.getReleaseDate(),
				mpaId // Передаем переменную вместо прямого вызова метода
		);
		film.setId(id);
		saveGenres(film);
		film.setGenres(getGenresByFilmId(film.getId()));
		return film;
//		return findById(film.getId())
//				.orElseThrow(() -> new NotFoundException("Фильм не найден после сохранения с id = " + id));
	}

	@Override
	public Film update(Film film) {
		// 1. Обновляем основные поля фильма в таблице films
		String sql = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?, ratingid = ? WHERE id = ?";

		int rowsUpdated = jdbc.update(sql,
				film.getName(),
				film.getDescription(),
				film.getReleaseDate(),
				film.getDuration(),
				film.getMpa().getId(),
				film.getId()
		);

		// Если база данных вернула 0 измененных строк, значит фильма с таким ID нет
		if (rowsUpdated == 0) {
			throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
		}

		// 2. Обновляем жанры в связующей таблице film_genre (метод сначала удалит старые, потом вставит новые)
		saveGenres(film);
		film.setGenres(getGenresByFilmId(film.getId()));

		return film;
		// 3. Запрашиваем из базы полностью пересобранный фильм с актуальным JOIN mpa и loadGenres
//		return findById(film.getId())
//				.orElseThrow(() -> new NotFoundException("Фильм не найден для обновления"));
	}

	@Override
	public Collection<Genre> getAllGenres() {
		String sql = "SELECT id, name FROM genre ORDER BY id";
		return jdbc.query(sql, this::makeGenre);
	}

	@Override
	public Optional<Genre> getGenreById(int id) {
		String sql = "SELECT id, name FROM genre WHERE id = ?";
		return jdbc.query(sql, this::makeGenre, id)
				.stream()
				.findFirst();
	}

	private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
		return new Genre(
				rs.getLong("id"),
				rs.getString("name")
		);
	}

	public boolean checkMpaExists(long mpaId) {
		String sql = "SELECT COUNT(*) FROM mpa WHERE id = ?"; // Убедитесь, что имена таблицы и колонки совпадают с вашей schema.sql
		Integer count = jdbc.queryForObject(sql, Integer.class, mpaId);
		return count != null && count > 0;
	}
	public Collection<Mpa> findAllMpa() {
		String sql = "SELECT * FROM mpa ORDER BY id";
		// Используйте ваш маппер для Mpa, если он есть, либо соберите на месте:
		return jdbc.query(sql, (rs, rowNum) -> {
			Mpa mpa = new Mpa();
			mpa.setId(rs.getLong("id"));
			mpa.setName(rs.getString("rating")); // или "name" в зависимости от вашей БД
			return mpa;
		});
	}

	public Optional<Mpa> findMpaById(Long id) {
		String sql = "SELECT * FROM mpa WHERE id = ?";
		try {
			Mpa mpa = jdbc.queryForObject(sql, (rs, rowNum) -> {
				Mpa mpaObj = new Mpa();
				mpaObj.setId(rs.getLong("id"));
				mpaObj.setName(rs.getString("rating"));
				return mpaObj;
			}, id);
			return Optional.ofNullable(mpa);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}



	public boolean checkMpaExists(Long mpaId) {
		if (mpaId == null) return false;
		String sql = "SELECT COUNT(*) FROM mpa WHERE id = ?";
		Integer count = jdbc.queryForObject(sql, Integer.class, mpaId);
		return count != null && count > 0;
	}

	public boolean checkGenreExists(Long genreId) {
		if (genreId == null) return false;
		String sql = "SELECT COUNT(*) FROM genre WHERE id = ?";
		Integer count = jdbc.queryForObject(sql, Integer.class, genreId);
		return count != null && count > 0;
	}

	private void saveGenres2(Film film) {
		String deleteSql = "DELETE FROM film_genre WHERE film_id = ?";
		jdbc.update(deleteSql, film.getId());

		if (film.getGenres() == null || film.getGenres().isEmpty()) {
			return;
		}

		// Сохраняем новые жанры
		String insertSql = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
		for (Genre genre : film.getGenres()) {
			jdbc.update(insertSql, film.getId(), genre.getId());
		}
	}




//	@Override
//	public Film save(Film film) {
//		long id = insert(
//				INSERT_QUERY,
//				film.getName(),
//				film.getDescription(),
//				film.getDuration(),
//				film.getReleaseDate(),
//
//				film.getMpa().getId()
//		);
//		film.setId(id);
//		saveGenres(film);
//		return findById(film.getId())
//				.orElseThrow(() -> new NotFoundException("Фильм не найден после сохранения с id = " + id));
//	}

//	@Override
//	public Film create(Film film) {
//		long id = insert(INSERT_QUERY, film.getName(), film.getDescription(),
//				film.getReleaseDate(), film.getDuration(), film.getMpa().getId());
//		film.setId(id);
//		saveGenres(film); // Сохранили жанры в БД
//
//		// Перезапрашиваем фильм из БД, чтобы сработал LEFT JOIN mpa и подгрузился loadGenres!
//		return findById(id)
//				.orElseThrow(() -> new NotFoundException("Фильм не найден после сохранения"));
//	}

	@Override
	public List<Film> getTopFilms(int count) {
		List<Film> popularFilms = findMany(FIND_POPULAR_QUERY, count);
		popularFilms.forEach(this::loadGenres);
		return popularFilms;
	}




	@Override
	public Optional<Film> findById(Long filmId) {
		Optional<Film> filmOptional = findOne(FIND_BY_ID_QUERY, filmId);

		// 2. Если фильм найден, подтягиваем и устанавливаем ему список жанров
		filmOptional.ifPresent(film -> film.setGenres(getGenresByFilmId(filmId)));

		return filmOptional;
//		String sql = "SELECT * FROM film WHERE id = ?"; // ваш текущий SQL запрос для фильма

//		Optional<Film> filmOptional = jdbc.query(sql, this::makeFilm, filmId) // или ваш RowMapper для фильма
//				.stream()
//				.findFirst();

		// Если фильм найден, подтягиваем и устанавливаем ему список жанров
//		filmOptional.ifPresent(film -> film.setGenres(getGenresByFilmId(filmId)));

//		return filmOptional;
//		Optional<Film> filmOptional = findOne(FIND_BY_ID_QUERY, filmId);
//		Optional<Film> filmOptional = findOne(FIND_BY_ID_QUERY, new FilmRowMapper(), filmId);
//		filmOptional.ifPresent(this::loadGenres);
//		return filmOptional;
	}

	private java.util.Set<Genre> getGenresByFilmId(long filmId) {
		String sql = "SELECT g.id, g.name FROM genre g " +
				"JOIN film_genre fg ON g.id = fg.genre_id " +
				"WHERE fg.film_id = ? " +
				"ORDER BY g.id";
		return new LinkedHashSet<>(jdbc.query(sql, new DataClassRowMapper<>(Genre.class), filmId));
	}

	public void loadGenres(Film film) {


		List<Genre> genres = jdbc.query(FIND_GENRES_BY_FILM_ID, (rs, rowNum) -> {
			Genre genre = new Genre();
			genre.setId(rs.getLong("id"));
			genre.setName(rs.getString("name")); // Ручное и безопасное чтение колонки name
			return genre;
		}, film.getId());

		film.setGenres(new LinkedHashSet<>(genres));
//		List<Genre> genres = jdbc.query(FIND_GENRES_BY_FILM_ID, new BeanPropertyRowMapper<>(Genre.class), film.getId());
//		film.setGenres(new LinkedHashSet<>(genres));
	}


//	@Override
//	public Optional<Film> findByName(String name) {
//		Optional<Film> filmOptional = findOne(FIND_BY_NAME_QUERY, name);
//		filmOptional.ifPresent(this::loadFriends);
//		return filmOptional;
//	}



	private void saveGenres(Film film) {
		if (film.getGenres() == null || film.getGenres().isEmpty()) {
			return;
		}
		String deleteSql = "DELETE FROM film_genre WHERE film_id = ?";
		jdbc.update(deleteSql, film.getId());
		String insertSql = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
		for (Genre genre : film.getGenres()) {
			jdbc.update(insertSql, film.getId(), genre.getId());
		}
	}

//	public Film createFilm(Film film) {
//		String sql = "INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?)";
//
//		KeyHolder keyHolder = new GeneratedKeyHolder();
//
//		jdbc.update(connection -> {
//			PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
//			ps.setString(1, film.getName());
//			ps.setString(2, film.getDescription());
//			ps.setDate(3, Date.valueOf(film.getReleaseDate()));
//			ps.setInt(4, film.getDuration());
//			ps.setLong(5, film.getMpa().getId());
//			return ps;
//		}, keyHolder);
//
//		// Вытаскиваем сгенерированный базой данных ID
//		Long filmId = Objects.requireNonNull(keyHolder.getKey()).longValue();
//		film.setId(filmId);
//
//		// Если у фильма при создании переданы жанры, не забудьте их тоже сохранить в связующую таблицу!
//		// saveGenres(film);
//
//		return film; // Возвращаем фильм с установленным ID
//	}

	@Override
	public Optional<Film> findFilmById(Long id) {
		try {
			Film film = jdbc.queryForObject(FIND_FILM_BY_ID_QUERY, mapper, id);

			// !!! ДОБАВИТЬ ЭТУ СТРОКУ ПЕРЕД RETURN !!!
			if (film != null) {
				film.setGenres(getGenresByFilmId(id));
			}

			return Optional.ofNullable(film);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}

//		try {
//			Film film = jdbc.queryForObject(FIND_FILM_BY_ID_QUERY, mapper, id);
//			return Optional.ofNullable(film);
//		} catch (EmptyResultDataAccessException e) {
//			return Optional.empty();
//		}
	}

	@Override
	public Collection<Film> findAll() {
//		String sql = "SELECT * FROM film";
//		Collection<Film> films = jdbc.query(sql, mapper); // укажите ваш маппер для фильмов
//
//		for (Film film : films) {
//			film.setGenres(getGenresByFilmId(film.getId()));
//		}
//
//		return films;
		List<Film> films = findMany(FIND_ALL_QUERY);
		films.forEach(this::loadGenres);
		return films;
	}






	@Override
	public Map<Long, Film> getFilms() {
		return Map.of();
	}


}
