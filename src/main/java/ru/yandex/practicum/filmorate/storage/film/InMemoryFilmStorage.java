package ru.yandex.practicum.filmorate.storage.film;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage{
	@Getter
	private final Map<Long, Film> films = new HashMap<>();
	private static final Logger log = LoggerFactory.getLogger(FilmController.class);

	@Override
	public Collection<Film> findAll() {
		return films.values();
	}

	@Override
	public Film create(Film film) {
		return saveFilm(film);
	}

	@Override
	public Film update(Film newFilm) {
		return updateFilm(newFilm);
	}

	private Film updateFilm(Film newFilm) {
		log.trace("проверяем необходимые условия");
		validateFilm(newFilm);
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
		log.trace("проверяем выполнение необходимых условий");
		validateFilm(film);
		log.trace("формируем дополнительные данные");
		film.setId(getNextId());
		log.trace("сохраняем новый фильм в памяти приложения");
		films.put(film.getId(), film);
		return film;
	}

	private void validateFilm(Film film) {
		if (film.getName() == null || film.getName().isBlank()) {
			logAndThrow("Название не может быть пустым");
		}
		if (film.getDescription().length() > 200) {
			logAndThrow("Максимальная длина описания — 200 символов");
		}
		if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
			logAndThrow("дата релиза должна быть не раньше 28 декабря 1895 года");
		}
		if (film.getDuration() <= 0) {
			logAndThrow("продолжительность фильма должна быть положительным числом");
		}
	}

	private void logAndThrow(String message) {
		log.error(message);
		throw new ConditionsNotMetException(message);
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
}
