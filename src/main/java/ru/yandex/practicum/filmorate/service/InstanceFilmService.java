package ru.yandex.practicum.filmorate.service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
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
		return filmStorage.findAllMpa();
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

	@Override
	public FilmDto createFilm(NewFilmRequest request) {
		validate(request);
		Film film = FilmMapper.mapToFilm(request);
		Film savedFilm = filmStorage.save(film);
		return FilmMapper.mapToFilmDto(savedFilm);
	}

	private void validate(NewFilmRequest request) {
		if (request.getMpa() != null) {
			boolean mpaExists = filmStorage.checkMpaExists(request.getMpa().getId());
			if (!mpaExists) {
				throw new NotFoundException("Рейтинг MPA с id = " + request.getMpa().getId() + " не найден");
			}
		}
		if (request.getName() == null || request.getName().isBlank()) {
			throw new ValidationException("Название фильма не может быть пустым");
		}
		LocalDate cinemaBirthday = LocalDate.of(1895, 12, 28);
		if (request.getReleaseDate() != null && request.getReleaseDate().isBefore(cinemaBirthday)) {
			throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
		}
		if (request.getDescription() != null && request.getDescription().length() > 200) {
			throw new ValidationException("Максимальная длина описания 200 символов");
		}
		if (request.getDuration() != null && request.getDuration() <= 0) {
			throw new ValidationException("Продолжительность фильма должна быть положительной");
		}
		if (request.getGenres() != null && !request.getGenres().isEmpty()) {
			for (Genre genre : request.getGenres()) {
				if (!filmStorage.checkGenreExists(genre.getId())) {
					throw new NotFoundException("Жанр с id = " + genre.getId() + " не существует.");
				}
			}
		}
	}

	@Override
	public void addLike(long filmId, @NotNull(message = "вы не указали id пользователя который поставил лайк") long userId) {
		filmStorage.findFilmById(filmId)
				.orElseThrow(() -> new NotFoundException("Фильм с id = " + filmId + " не найден"));
		userStorage.findById(userId)
				.orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
		filmStorage.addLike(filmId, userId);
		filmStorage.findFilmById(filmId).get().addLike(userId);
	}

	@Override
	public void delLike(long filmId, long userId) {
		filmStorage.findFilmById(filmId)
				.orElseThrow(() -> new NotFoundException("Фильм с id = " + filmId + " не найден"));
		userStorage.findById(userId)
				.orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
		filmStorage.removeLike(filmId, userId);
	}

	@Override
	public Collection<Film> findAll() {
		return filmStorage.findAll();
	}

	@Override
	public FilmDto update(UpdateFilmRequest request) {
		filmStorage.findById(request.getId())
				.orElseThrow(() -> new NotFoundException("Фильм с id = " + request.getId() + " не найден"));
		Film film = FilmMapper.mapToFilm(request);
		Film updatedFilm = filmStorage.update(film);
		return FilmMapper.mapToFilmDto(updatedFilm);
	}

	@Override
	public Film findFilmById(Long id) {
		return filmStorage.findFilmById(id).get();
	}
}
