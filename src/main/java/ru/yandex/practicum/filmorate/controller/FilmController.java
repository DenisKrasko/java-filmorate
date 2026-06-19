package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/films")
public class FilmController {
	@Getter
	private final Map<Long, Film> films = new HashMap<>();
	private static final Logger log = LoggerFactory.getLogger(FilmController.class);

	@GetMapping
	public Collection<Film> findAll() {
		return films.values();
	}

	@PostMapping
	public Film create(@Valid @RequestBody Film film) {
		return saveFilm(film);
	}

	@PutMapping
	public Film update(@Valid @RequestBody Film newFilm) {
		return updateFilm(newFilm);
	}

	private Film updateFilm(Film newFilm) {
		if (newFilm.getId() == null) {
			throw new ConditionsNotMetException("Id должен быть указан");
		}
		if (films.containsKey(newFilm.getId())) {
			Film oldFilm = films.get(newFilm.getId());
			log.trace("фильм найден и все условия соблюдены, обновляем содержимое");
			oldFilm.setDescription(newFilm.getDescription());
			oldFilm.setName(newFilm.getName());
			oldFilm.setDuration(newFilm.getDuration());
			oldFilm.setReleaseDate(newFilm.getReleaseDate());

			return oldFilm;
		}
		log.error("Фильм с id = {} не найден", newFilm.getId());
		throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
	}

	private Film saveFilm(Film film) {
		log.trace("формируем дополнительные данные");
		film.setId(getNextId());
		log.trace("сохраняем новый фильм в памяти приложения");
		films.put(film.getId(), film);
		return film;
	}

	/**
	 * Вспомогательный метод для генерации идентификатора нового поста
	 */
	private long getNextId() {
		long currentMaxId = films.keySet()
				.stream()
				.mapToLong(id -> id)
				.max()
				.orElse(0);
		return ++currentMaxId;
	}

	private void logAndThrow(String message) {
		log.error(message);
		throw new ConditionsNotMetException(message);
	}
}