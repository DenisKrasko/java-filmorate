package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.List;

public interface FilmService {
	Collection<Genre> getAllGenres();

	Genre getGenreById(int id);

	Collection<Mpa> findAllMpa();

	Mpa findMpaById(Long id);

	List<FilmDto> getPopularFilms(int count);

	void addLike(long filmId, long userId);

	void delLike(long filmId, long userId);

	Collection<Film> findAll();

	FilmDto createFilm(NewFilmRequest filmRequest);

	FilmDto update(UpdateFilmRequest request);

	Film findFilmById(Long id);
}