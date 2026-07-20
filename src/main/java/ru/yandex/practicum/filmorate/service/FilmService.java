package ru.yandex.practicum.filmorate.service;

import jakarta.validation.constraints.NotNull;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

public interface FilmService {
	Collection<Genre> getAllGenres();

	Genre getGenreById(int id);

	Collection<Mpa> findAllMpa();

	Mpa findMpaById(Long id);

//	FilmDto updateFilm(UpdateFilmRequest request);

	List<FilmDto> getPopularFilms(int count);

	void addLike(long filmId, long userId);

	void delLike(long filmId, long userId);

	Collection<Film> findAll();

	FilmDto createFilm(NewFilmRequest filmRequest);

	FilmDto update(UpdateFilmRequest request);
//	Film update(Film newFilm);

	Film findFilmById(Long id);

	FilmStorage getFilmStorage();

	UserStorage getUserStorage();
}