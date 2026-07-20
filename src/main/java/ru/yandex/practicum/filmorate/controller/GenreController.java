package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/genres") // Важно: множественное число!
@RequiredArgsConstructor
public class GenreController {

	private final FilmService filmService;

	@GetMapping
	public Collection<Genre> getAllGenres() {
		return filmService.getAllGenres();
	}

	@GetMapping("/{id}")
	public Genre getGenreById(@PathVariable int id) {
		return filmService.getGenreById(id);
	}
}