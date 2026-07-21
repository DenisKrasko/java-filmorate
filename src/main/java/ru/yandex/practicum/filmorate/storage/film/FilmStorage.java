package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
	void addLike(Long filmId, Long userId);

	void removeLike(Long filmId, Long userId);

	Collection<Genre> getAllGenres();

	Optional<Genre> getGenreById(int id);

	Collection<Mpa> findAllMpa();

	Optional<Mpa> findMpaById(Long id);

	boolean checkMpaExists(Long mpaId);

	boolean checkGenreExists(Long genreId);

	List<Film> getTopFilms(int count);

	Optional<Film> findById(Long filmId);

	Film save(Film film);

	Collection<Film> findAll();

	Film update(Film film);

	Optional<Film> findFilmById(Long id);
}