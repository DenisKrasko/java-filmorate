package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
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
	private int duration;
	private Set<Long> likes = new HashSet<>();

	public void addLike(long userId) {
		likes.add(userId);
	}

	public void delLike(long userId) {
		likes.remove(userId);
	}
}