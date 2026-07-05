package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
	private final InMemoryFilmStorage filmStorage;
	private static final Logger log = LoggerFactory.getLogger(FilmController.class);
	private final FilmService filmService;

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

	@PutMapping("/{id}/like/{userId}")
	public void addLike(@PathVariable("id") long id,
						@PathVariable(required = false, value = "userId") String userId) {
		if (userId != null) {
			filmService.addLike(id, Long.valueOf(userId));
		} else {
			throw new NotFoundException("вы не указали id пользователя который поставил лайк");
		}
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
		return filmStorage.findFilmById(id);
	}
}