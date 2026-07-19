package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Film.
 */
@Data
public class Film {
	@EqualsAndHashCode.Exclude
	private Long id;
	private String name;
	private String description;
	@JsonFormat
	private LocalDate releaseDate;
	private Long duration;
	private Set<Long> likes = new HashSet<>();
	private Mpa mpa;
	private List<Genre> genres;

	public void addLike(long userId) {
		likes.add(userId);
	}

	public void delLike(long userId) {
		likes.remove(userId);
	}
}