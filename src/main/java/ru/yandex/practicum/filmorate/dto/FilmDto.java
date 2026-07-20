package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Data
public class FilmDto {
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long id;
	private String name;
	private String description;
	private LocalDate releaseDate;
	private Long duration;
	private List<Long> likes = new ArrayList<>();
	private Mpa mpa;
	private Set<Genre> genres = new LinkedHashSet<>();
}
