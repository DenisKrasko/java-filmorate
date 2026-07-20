package ru.yandex.practicum.filmorate.service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstanceFilmService implements FilmService {
	private final FilmStorage filmStorage;
	private final UserStorage userStorage;

	@Override
	public Collection<Genre> getAllGenres() {
		return filmStorage.getAllGenres();
	}

	@Override
	public Genre getGenreById(int id) {
		return filmStorage.getGenreById(id)
				.orElseThrow(() -> new NotFoundException("Жанр с id " + id + " не найден"));
	}

	@Override
	public Collection<Mpa> findAllMpa() {
		return filmStorage.findAllMpa(); // Метод в сторидже делает SELECT * FROM mpa
	}

	@Override
	public Mpa findMpaById(Long id) {
		return filmStorage.findMpaById(id)
				.orElseThrow(() -> new NotFoundException("Рейтинг MPA с id = " + id + " не найден"));
	}


	@Override
	public List<FilmDto> getPopularFilms(int count) {
		return filmStorage.getTopFilms(count).stream()
				.map(FilmMapper::mapToFilmDto)
				.collect(Collectors.toList());
	}
//	@Override
//	public List<Film> getPopularFilms(int count) {
//		List<Film> popularFilms = filmStorage.getFilms().entrySet().stream()
//				.sorted(Map.Entry.<Long, Film>comparingByValue(
//						Comparator.comparingInt((Film film) -> film.getLikes().size())
//								.reversed()
//				))
//				.map(Map.Entry::getValue)
//				.limit(count)
//				.collect(Collectors.toCollection(ArrayList::new));
//		return popularFilms;
//	}


	@Override
	public FilmDto createFilm(NewFilmRequest request) {
//		// 1. Проверяем валидность жанров перед сохранением
//		if (request.getGenres() != null && !request.getGenres().isEmpty()) {
//			for (Genre genre : request.getGenres()) {
//				// Запрашиваем каждый жанр из базы через сторадж жанров (genreStorage)
//				filmStorage.genreStorage.findById(genre.getId())
//						.orElseThrow(() -> new ConditionsNotMetException("Жанр с id = " + genre.getId() + " не существует"));
//				// Внимание: Если тесты требуют статус 400, выбрасывайте исключение ValidationException или ConditionsNotMetException
//			}
//		}
		validate(request);
		Film film = FilmMapper.mapToFilm(request);
		Film savedFilm = filmStorage.save(film);
		return FilmMapper.mapToFilmDto(savedFilm);


//		Film film = FilmMapper.mapToFilm(request);
//		Film savedFilm = filmStorage.save(film);
//		Film savedFilm = filmStorage.save(film);
//		return FilmMapper.mapToFilmDto(savedFilm);
//		if (request.getEmail() == null || request.getEmail().isEmpty()) {
//			throw new ConditionsNotMetException("Имейл должен быть указан");
//		}
//		if (request.getLogin() == null || request.getLogin().isBlank() || request.getLogin().contains(" ")) {
//			throw new ValidationException("Логин не может быть пустым или содержать пробелы");
//		}
//		if (!request.getEmail().contains("@")) {
//			throw new ValidationException("Имейл должен содержать символ @");
//		}
//		if (request.getBirthday() != null && request.getBirthday().isAfter(LocalDate.now())) {
//			throw new ValidationException("Дата рождения не может быть в будущем");
//		}
//		Optional<Film> alreadyExistUser = filmStorage.findByEmail(request.getEmail());
//		if (alreadyExistUser.isPresent()) {
//			throw new DuplicatedDataException("Данный имейл уже используется");
//		}
	}

	private void validate(NewFilmRequest request) {
		if (request.getMpa() != null) {
			boolean mpaExists = filmStorage.checkMpaExists(request.getMpa().getId());
			if (!mpaExists) {
				throw new NotFoundException("Рейтинг MPA с id = " + request.getMpa().getId() + " не найден.");
			}
		}
		if (request.getName() == null || request.getName().isBlank()) {
			throw new ValidationException("Название фильма не может быть пустым.");
		}
		LocalDate cinemaBirthday = LocalDate.of(1895, 12, 28);
		if (request.getReleaseDate() != null && request.getReleaseDate().isBefore(cinemaBirthday)) {
			throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.");
		}
		if (request.getDescription() != null && request.getDescription().length() > 200) {
			throw new ValidationException("Максимальная длина описания — 200 символов.");
		}
		if (request.getDuration() != null && request.getDuration() <= 0) {
			throw new ValidationException("Продолжительность фильма должна быть положительной.");
		}
//		if (request.getRating_MPA_id() != null) {
//			if (!filmStorage.checkMpaExists(request.getRating_MPA_id().getId())) {
//				throw new NotFoundException("Рейтинг MPA с id = " + request.getRating_MPA_id() + " не существует.");
//			}
//		}
		if (request.getGenres() != null && !request.getGenres().isEmpty()) {
			for (Genre genre : request.getGenres()) {
				if (!filmStorage.checkGenreExists(genre.getId())) {
					throw new NotFoundException("Жанр с id = " + genre.getId() + " не существует.");
				}
			}
		}
	}






//		Film film = FilmMapper.mapToUser(request);
//		user = userStorage.save(user);
//		return UserMapper.mapToUserDto(user);
//		// 1. Валидация входных данных (если она есть)
//		// validation(filmDto);
//
//		// 2. Маппим DTO в модель Film
//		Film film = FilmMapper.mapToFilm(filmDto);
//
//		// 3. Сохраняем в БД и обязательно получаем обратно сохраненный фильм с присвоенным ID
//		Film savedFilm = filmStorage.createFilm(film);
//
//		// 4. Маппим обратно в DTO и ВОЗВРАЩАЕМ
//		return FilmMapper.mapToFilmDto(savedFilm);
////		return filmStorage.create(film);


	@Override
	public void addLike(long filmId, @NotNull(message = "вы не указали id пользователя который поставил лайк") long userId) {
		filmStorage.findFilmById(filmId)
				.orElseThrow(() -> new NotFoundException("Фильм с id = " + filmId + " не найден"));
		userStorage.findById(userId) // или как у вас называется метод в userStorage
				.orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));

		// 3. Вызываем хранилище для записи лайка в БД
		filmStorage.addLike(filmId, userId);

//		if (!filmStorage.getFilms().containsKey(filmId)) {
//			throw new NotFoundException("Фильм с id = " + filmId + ", для которого нужно добавить лайк, не найден");
//		}
//		if (!userStorage.getUsers().containsKey(userId)) {
//			throw new NotFoundException("Пользователь с id = " + userId + ", который ставит лайк фильму, не найден");
//		}
		filmStorage.findFilmById(filmId).get().addLike(userId);
	}

	@Override
	public void delLike(long filmId, long userId) {
		filmStorage.findFilmById(filmId)
				.orElseThrow(() -> new NotFoundException("Фильм с id = " + filmId + " не найден"));
		userStorage.findById(userId)
				.orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
		filmStorage.removeLike(filmId, userId);
//		if (!filmStorage.getFilms().containsKey(filmId)) {
//			throw new NotFoundException("Фильм с id = " + filmId + ", для которого нужно удалить лайк, не найден");
//		}
//		if (!userStorage.getUsers().containsKey(userId)) {
//			throw new NotFoundException("Пользователь с id = " + userId + ", лайк которого нужно удалить, не найден");
//		}
//		if (!filmStorage.getFilms().get(filmId).getLikes().contains(userId)) {
//			throw new NotFoundException("В фильме с id = " + userId + " нету лайка от пользователя с id = " + userId);
//		}
//		filmStorage.findFilmById(filmId).get().delLike(userId);
	}



	@Override
	public Collection<Film> findAll() {
		return filmStorage.findAll();
	}


	@Override
	public FilmDto update(UpdateFilmRequest request) {
//		Film film = FilmMapper.mapToFilm(request);
//
//		// !!! ДОБАВИТЬ ЭТУ СТРОКУ !!!
//		// Явно переносим жанры из запроса в объект фильма, если это не делает маппер
//		if (request.getGenres() != null) {
//			film.setGenres(new java.util.LinkedHashSet<>(request.getGenres()));
//		}
//
//		// 2. Обновляем в хранилище (внутри FilmDbStorage у вас уже вызывается saveGenres)
//		Film updatedFilm = filmStorage.update(film);
//
//		// 3. Возвращаем DTO
//		return FilmMapper.mapToFilmDto(updatedFilm);

		filmStorage.findById(request.getId())
				.orElseThrow(() -> new NotFoundException("Фильм с id = " + request.getId() + " не найден"));
		Film film = FilmMapper.mapToFilm(request); // Убедитесь, что у вас есть маппинг для UpdateFilmRequest
		Film updatedFilm = filmStorage.update(film);
		return FilmMapper.mapToFilmDto(updatedFilm);
	}

	@Override
	public Film findFilmById(Long id) {
		return filmStorage.findFilmById(id).get();
	}

	@Override
	public FilmStorage getFilmStorage() {
		return filmStorage;
	}

	@Override
	public UserStorage getUserStorage() {
		return userStorage;
	}

}
