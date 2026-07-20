package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.*;

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
	private List<Long> likes = new ArrayList<>();
	private Mpa mpa;
	private Set<Genre> genres = new LinkedHashSet<>();

	public void addLike(long userId) {
		likes.add(userId);
	}

	public void delLike(long userId) {
		likes.remove(userId);
	}
}