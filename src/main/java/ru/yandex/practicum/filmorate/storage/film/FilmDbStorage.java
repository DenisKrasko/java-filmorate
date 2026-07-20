package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {
	private static final String COUNT_GENRES_BY_ID = "SELECT COUNT(*) FROM genre WHERE id = ?";
	private static final String COUNT_MPA_BY_ID = "SELECT COUNT(*) FROM mpa WHERE id = ?";
	private static final String FILM_GENRE_DELETE_BY_FILM_ID = "DELETE FROM film_genre WHERE film_id = ?";
	private static final String GENRE_FIND_BY_FILM_ID = "SELECT g.id, g.name FROM genre g " +
			"JOIN film_genre fg ON g.id = fg.genre_id " +
			"WHERE fg.film_id = ? " +
			"ORDER BY g.id";
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
					"LEFT JOIN storage_user_film_like l ON f.id = l.film_id " +
					"GROUP BY f.id, m.rating " +
					"ORDER BY likes_count DESC " +
					"LIMIT ?";
	private static final String FIND_GENRES_BY_FILM_ID = "SELECT g.id, g.name FROM genre g " +
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
	private static final String FIND_BY_NAME_QUERY = "SELECT * FROM film WHERE name = ?";
	private static final String INSERT_QUERY = """
			INSERT INTO film (name, description, duration, release_date, ratingid) 
			VALUES (?, ?, ?, ?, ?)
			""";

	public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
		super(jdbc, mapper);
	}

	@Override
	public void addLike(Long filmId, Long userId) {
		String sql = "INSERT INTO storage_user_film_like (film_id, user_id) VALUES (?, ?)";
		jdbc.update(sql, filmId, userId);
	}

	@Override
	public void removeLike(Long filmId, Long userId) {
		String sql = "DELETE FROM storage_user_film_like WHERE film_id = ? AND user_id = ?";
		int rowsAffected = jdbc.update(sql, filmId, userId);
		if (rowsAffected == 0) {
			throw new NotFoundException("Лайк от пользователя " + userId + " не найден");
		}
	}

	@Override
	public Film save(Film film) {
		Long mpaId = (film.getMpa() != null) ? film.getMpa().getId() : null;
		long id = insert(
				INSERT_QUERY,
				film.getName(),
				film.getDescription(),
				film.getDuration(),
				film.getReleaseDate(),
				mpaId
		);
		film.setId(id);
		saveGenres(film);
		film.setGenres(getGenresByFilmId(film.getId()));
		return film;
	}

	@Override
	public Film update(Film film) {
		String sql = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?, ratingid = ? WHERE id = ?";
		int rowsUpdated = jdbc.update(sql,
				film.getName(),
				film.getDescription(),
				film.getReleaseDate(),
				film.getDuration(),
				film.getMpa().getId(),
				film.getId()
		);
		if (rowsUpdated == 0) {
			throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
		}
		saveGenres(film);
		film.setGenres(getGenresByFilmId(film.getId()));
		return film;
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
		String sql = "SELECT COUNT(*) FROM mpa WHERE id = ?";
		Integer count = jdbc.queryForObject(sql, Integer.class, mpaId);
		return count != null && count > 0;
	}

	public Collection<Mpa> findAllMpa() {
		String sql = "SELECT * FROM mpa ORDER BY id";
		return jdbc.query(sql, (rs, rowNum) -> {
			Mpa mpa = new Mpa();
			mpa.setId(rs.getLong("id"));
			mpa.setName(rs.getString("rating"));
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
		Integer count = jdbc.queryForObject(COUNT_MPA_BY_ID, Integer.class, mpaId);
		return count != null && count > 0;
	}

	public boolean checkGenreExists(Long genreId) {
		if (genreId == null) return false;
		Integer count = jdbc.queryForObject(COUNT_GENRES_BY_ID, Integer.class, genreId);
		return count != null && count > 0;
	}

	@Override
	public List<Film> getTopFilms(int count) {
		List<Film> popularFilms = findMany(FIND_POPULAR_QUERY, count);
		popularFilms.forEach(this::loadGenres);
		return popularFilms;
	}

	@Override
	public Optional<Film> findById(Long filmId) {
		Optional<Film> filmOptional = findOne(FIND_BY_ID_QUERY, filmId);
		filmOptional.ifPresent(film -> film.setGenres(getGenresByFilmId(filmId)));
		return filmOptional;
	}

	private java.util.Set<Genre> getGenresByFilmId(long filmId) {
		return new LinkedHashSet<>(jdbc.query(GENRE_FIND_BY_FILM_ID, new DataClassRowMapper<>(Genre.class), filmId));
	}

	public void loadGenres(Film film) {
		List<Genre> genres = jdbc.query(FIND_GENRES_BY_FILM_ID, (rs, rowNum) -> {
			Genre genre = new Genre();
			genre.setId(rs.getLong("id"));
			genre.setName(rs.getString("name"));
			return genre;
		}, film.getId());

		film.setGenres(new LinkedHashSet<>(genres));
	}

	private void saveGenres(Film film) {
		if (film.getGenres() == null || film.getGenres().isEmpty()) {
			return;
		}
		jdbc.update(FILM_GENRE_DELETE_BY_FILM_ID, film.getId());
		String insertSql = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
		for (Genre genre : film.getGenres()) {
			jdbc.update(insertSql, film.getId(), genre.getId());
		}
	}

	@Override
	public Optional<Film> findFilmById(Long id) {
		try {
			Film film = jdbc.queryForObject(FIND_FILM_BY_ID_QUERY, mapper, id);
			if (film != null) {
				film.setGenres(getGenresByFilmId(id));
			}
			return Optional.ofNullable(film);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	@Override
	public Collection<Film> findAll() {
		List<Film> films = findMany(FIND_ALL_QUERY);
		films.forEach(this::loadGenres);
		return films;
	}
}