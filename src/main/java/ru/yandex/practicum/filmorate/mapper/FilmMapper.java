package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmMapper {
	public static Film mapToFilm(NewFilmRequest request) {
		Film film = new Film();
		film.setName(request.getName());
		film.setDescription(request.getDescription());
		film.setReleaseDate(request.getReleaseDate());
		film.setDuration(request.getDuration());
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

	public static Film mapToFilm(UpdateFilmRequest request) {
		Film film = new Film();
		film.setId(request.getId());
		film.setName(request.getName());
		film.setDescription(request.getDescription());
		film.setReleaseDate(request.getReleaseDate());
		film.setDuration(request.getDuration());
		Mpa mpa = new Mpa();
		mpa.setId(request.getRating());
		film.setMpa(mpa);
		if (request.getGenres() != null) {
			film.setGenres(request.getGenres());
		}
		return film;
	}
}