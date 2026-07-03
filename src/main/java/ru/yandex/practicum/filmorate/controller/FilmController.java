package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {
	private final InMemoryFilmStorage filmStorage;
	private static final Logger log = LoggerFactory.getLogger(FilmController.class);

	@Autowired
	public FilmController(InMemoryFilmStorage filmStorage) {
		this.filmStorage = filmStorage;
	}

	@GetMapping
	public Collection<Film> findAll() {
		return filmStorage.findAll();
	}

	@PostMapping
	public Film create(@RequestBody Film film) {
		return filmStorage.create(film);
	}

	@PutMapping
	public Film update(@RequestBody Film newFilm) {
		return filmStorage.update(newFilm);
	}
}