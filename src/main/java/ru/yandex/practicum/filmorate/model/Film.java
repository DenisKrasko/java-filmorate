package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {
	@EqualsAndHashCode.Exclude
	private Long id;
	private String name;
	private String description;
	private LocalDate releaseDate;
	private int duration;

	public void setReleaseDate(String dateStr) {
		if (dateStr == null || dateStr.isEmpty()) {
			this.releaseDate = null;
		} else {
			this.releaseDate = LocalDate.parse(dateStr);
		}
	}

	public void setReleaseDate(LocalDate date) {
		this.releaseDate = date;
	}
}
