package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
	private static final Logger log = LoggerFactory.getLogger(FilmController.class);
	private final FilmService filmService;

	@GetMapping
	public Collection<Film> findAll() {
		return filmService.findAll();
	}

	@PostMapping
	public Film create(@RequestBody Film film) {
		return filmService.create(film);
	}

	@PutMapping
	public Film update(@RequestBody Film newFilm) {
		return filmService.update(newFilm);
	}

	@PutMapping("/{id}/like/{userId}")
	public void addLike(@PathVariable("id") long id,
						@PathVariable(required = false, value = "userId") long userId) {
		filmService.addLike(id, userId);
	}

	@DeleteMapping("/{id}/like/{userId}")
	public void delFriend(@PathVariable("id") long id,
						  @PathVariable("userId") long userId) {
		filmService.delLike(id, userId);
	}

	@GetMapping("/popular")
	public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") String count) {
		return filmService.getPopularFilms(Integer.valueOf(count));
	}

	@GetMapping("/{id}")
	public Film getFilm(@PathVariable("id") long id) {
		return filmService.findFilmById(id);
	}

	public FilmService getFilmService() {
		return filmService;
	}
}