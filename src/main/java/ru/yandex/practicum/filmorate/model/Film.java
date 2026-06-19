package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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
	@JsonFormat
	private LocalDate releaseDate;
	private int duration;
}