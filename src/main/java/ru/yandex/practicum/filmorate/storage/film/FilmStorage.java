package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface FilmStorage {
	Collection<Film> findAll();

	Film create(Film film);

	Film update(Film film);

	Map<Long, Film> getFilms();

	Optional<Film> findFilmById(Long id);
}