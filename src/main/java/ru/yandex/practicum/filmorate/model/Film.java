package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {
	@EqualsAndHashCode.Exclude
	private Long id;
	@NotBlank(message = "Название не может быть пустым")
	private String name;
	@Size(message = "Максимальная длина описания — 200 символов")
	private String description;
	@AfterCinemaBirthday(message = "Дата релиза должна быть не раньше 28 декабря 1895 года")
	private LocalDate releaseDate;
	@Positive(message = "Продолжительность фильма должна быть положительным числом")
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
