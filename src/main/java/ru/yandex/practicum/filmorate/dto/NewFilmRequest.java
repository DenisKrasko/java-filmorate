package ru.yandex.practicum.filmorate.dto;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class NewFilmRequest {
	private String name;
	private String description;
	private Long duration;
	private LocalDate releaseDate;
	private Mpa mpa;
	private Set<Genre> genres = new LinkedHashSet<>();
}