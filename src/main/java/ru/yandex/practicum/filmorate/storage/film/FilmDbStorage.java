package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.mappers.FilmRowMapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
	private final JdbcTemplate jdbcTemplate;
	private final FilmRowMapper filmRowMapper;

	private static final String FIND_FILM_BY_ID_QUERY = """
			SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rating_MPA_id AS mpa_id,
			       m.rating_MPA as mpa_name
			FROM film f
			LEFT JOIN mpa m ON f.rating_MPA_id = m.id
			WHERE f.id = ?
			""";

	@Override
	public Optional<Film> findFilmById(Long id) {
		try {
			Film film = jdbcTemplate.queryForObject(FIND_FILM_BY_ID_QUERY, filmRowMapper, id);
			return Optional.ofNullable(film);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	@Override
	public Collection<Film> findAll() {
		return List.of();
	}

	@Override
	public Film create(Film film) {
		return null;
	}

	@Override
	public Film update(Film film) {
		return null;
	}

	@Override
	public Map<Long, Film> getFilms() {
		return Map.of();
	}


}
