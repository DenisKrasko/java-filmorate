package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
	private final FilmService filmService;

	@PostMapping
	public FilmDto createFilm(@Valid @RequestBody NewFilmRequest filmRequest) {
		return filmService.createFilm(filmRequest);
	}

	@GetMapping
	public Collection<Film> findAll() {
		return filmService.findAll();
	}

	@PutMapping
	public FilmDto update(@Valid @RequestBody UpdateFilmRequest request) {
		return filmService.update(request);
	}

	@PutMapping("/{id}/like/{userId}")
	public void addLike(@PathVariable("id") Long id,
						@PathVariable(required = false, value = "userId") long userId) {
		filmService.addLike(id, userId);
	}

	@DeleteMapping("/{id}/like/{userId}")
	public void delFriend(@PathVariable("id") long id,
						  @PathVariable("userId") long userId) {
		filmService.delLike(id, userId);
	}

	@GetMapping("/popular")
	public List<FilmDto> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
		return filmService.getPopularFilms(count);
	}

	@GetMapping("/{id}")
	public Film getFilm(@PathVariable("id") long id) {
		return filmService.findFilmById(id);
	}

	public FilmService getFilmService() {
		return filmService;
	}
}