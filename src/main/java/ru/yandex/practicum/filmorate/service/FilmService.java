package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
	private final FilmStorage filmStorage;
	private final UserStorage userStorage;

	public void addLike(long filmId, long userId) {
		if (!filmStorage.getFilms().containsKey(filmId)) {
			throw new NotFoundException("Фильм с id = " + filmId + ", для которого нужно добавить лайк, не найден");
		}
		if (!userStorage.getUsers().containsKey(userId)) {
			throw new NotFoundException("Пользователь с id = " + userId + ", который ставит лайк фильму, не найден");
		}
		filmStorage.findFilmById(filmId).addLike(userId);
	}

	public void delLike(long filmId, long userId) {
		if (!filmStorage.getFilms().containsKey(filmId)) {
			throw new NotFoundException("Фильм с id = " + filmId + ", для которого нужно удалить лайк, не найден");
		}
		if (!userStorage.getUsers().containsKey(userId)) {
			throw new NotFoundException("Пользователь с id = " + userId + ", лайк которого нужно удалить, не найден");
		}
		if (!filmStorage.getFilms().get(filmId).getLikes().contains(userId)) {
			throw new NotFoundException("В фильме с id = " + userId + " нету лайка от пользователя с id = " + userId);
		}
		filmStorage.findFilmById(filmId).delLike(userId);
	}

	public List<Film> getPopularFilms(int count) {
		List<Film> popularFilms = filmStorage.getFilms().entrySet().stream()
				.sorted(Map.Entry.<Long, Film>comparingByValue(
						Comparator.comparingInt((Film film) -> film.getLikes().size())
								.reversed()
				))
				.map(Map.Entry::getValue)
				.limit(count)
				.collect(Collectors.toCollection(ArrayList::new));
		return popularFilms;
	}
}