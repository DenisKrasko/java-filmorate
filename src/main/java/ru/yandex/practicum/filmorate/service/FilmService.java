package ru.yandex.practicum.filmorate.service;

import jakarta.validation.constraints.NotNull;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

public interface FilmService {

	void addLike(long filmId, @NotNull(message = "вы не указали id пользователя который поставил лайк") long userId);

	void delLike(long filmId, long userId);

	List<Film> getPopularFilms(int count);

	Collection<Film> findAll();

	Film create(Film film);

	Film update(Film newFilm);

	Film findFilmById(Long id);

	FilmStorage getFilmStorage();

	UserStorage getUserStorage();
}