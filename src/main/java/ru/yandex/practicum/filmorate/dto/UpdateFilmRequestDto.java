package ru.yandex.practicum.filmorate.dto;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.model.Genre;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
public class UpdateFilmRequestDto {
	private Long id;
	@Setter
	private String name;
	@Setter
	private String description;
	private Long duration;
	@Setter
	private LocalDate releaseDate;
	private Set<Genre> genres = new LinkedHashSet<>();
	@Setter
	private Long rating;


	public boolean hasUsername() {
		return !(name == null || name.isBlank());
	}

	public boolean hasDuration() {
		return !(duration == null);
	}

	public boolean hasDescription() {
		return !(description == null || description.isBlank());
	}

	public boolean hasreleaseDate() {
		return !(releaseDate == null || releaseDate.isAfter(LocalDate.now()));
	}
}
