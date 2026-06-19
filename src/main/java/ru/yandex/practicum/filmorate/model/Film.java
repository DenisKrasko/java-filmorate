package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.xml.transform.Source;
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
	@Size(max = 200, message = "Максимальная длина описания — 200 символов")
	private String description;
	@NotNull(message = "Дата релиза обязательна")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate releaseDate;
	@Positive(message = "Продолжительность фильма должна быть положительным числом")
	private int duration;

}
