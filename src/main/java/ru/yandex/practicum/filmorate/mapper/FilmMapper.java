package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.util.LinkedHashSet;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmMapper {
	public static Film mapToFilm(NewFilmRequest request) {
		Film film = new Film();
		film.setName(request.getName());
		film.setDescription(request.getDescription());
		film.setReleaseDate(request.getReleaseDate());
		film.setDuration(request.getDuration());
//		Mpa mpa = new Mpa();
//		mpa.setId(request.getRating_MPA_id());
		film.setMpa(request.getMpa());
		if (request.getGenres() != null) {
			film.setGenres(request.getGenres());
		}
		return film;
	}

	public static FilmDto mapToFilmDto(Film film) {
		FilmDto dto = new FilmDto();
		dto.setId(film.getId());
		dto.setName(film.getName());
		dto.setDescription(film.getDescription());
		dto.setDuration(film.getDuration());
		dto.setReleaseDate(film.getReleaseDate());
		dto.setLikes(film.getLikes());
		dto.setGenres(film.getGenres());
		dto.setMpa(film.getMpa());
		return dto;
	}

	public static Film updateFilmFields(Film film, UpdateFilmRequest request) {
		if (request.hasDescription()) {
			film.setDescription(request.getDescription());
		}
		if (request.hasUsername()) {
			film.setName(request.getName());
		}
		if (request.hasDuration()) {
			film.setDuration(request.getDuration());
		}
		if (request.hasreleaseDate()) {
			film.setReleaseDate(request.getReleaseDate());
		}
		return film;
	}

	public static  Film mapToFilm(UpdateFilmRequest request) {
		Film film = new Film();
		// 1. Обязательно сетим id, так как это запрос на обновление существующего фильма!
		film.setId(request.getId());

		// 2. Копируем все остальные базовые поля
		film.setName(request.getName());
		film.setDescription(request.getDescription());
		film.setReleaseDate(request.getReleaseDate());
		film.setDuration(request.getDuration());

		// 3. Создаем и заполняем объект MPA
		Mpa mpa = new Mpa();
		mpa.setId(request.getRating_MPA_id());
		film.setMpa(mpa);

		// 4. Переносим жанры, если они переданы в запросе
		if (request.getGenres() != null) {
			film.setGenres(request.getGenres());
		}

		return film;
	}

}
