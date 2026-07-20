package ru.yandex.practicum.filmorate.storage.film.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmRowMapper implements RowMapper<Film> {
	@Override
	public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		Film film = new Film();
		film.setId(resultSet.getLong("id"));
		film.setName(resultSet.getString("name"));
		film.setDescription(resultSet.getString("description"));
		Date releaseDate = resultSet.getDate("release_date");
		if (releaseDate != null) {
			film.setReleaseDate(releaseDate.toLocalDate());
		}
		film.setDuration(resultSet.getLong("duration"));
		Mpa mpa = new Mpa();
		mpa.setId(resultSet.getLong("ratingid"));
//		mpa.setId(resultSet.getLong("mpa_id"));
		mpa.setName(resultSet.getString("rating"));
//		mpa.setRating_MPA(resultSet.getString("mpa_name"));
		film.setMpa(mpa);
		return film;
	}
}
