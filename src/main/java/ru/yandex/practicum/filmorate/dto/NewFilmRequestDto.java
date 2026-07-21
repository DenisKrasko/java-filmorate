package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.validator.MinimumDate;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class NewFilmRequestDto {
	@NotBlank(message = "Название фильма не может быть пустым")
	private String name;
	@Size(max = 200, message = "Макс длина описания 200 символов")
	private String description;
	@Positive(message = "Продолжительность фильма должна быть положительной")
	private Long duration;
	@NotNull(message = "Дата релиза не может быть пустой")
	@MinimumDate
	private LocalDate releaseDate;
	private Mpa mpa;
	private Set<Genre> genres = new LinkedHashSet<>();
}