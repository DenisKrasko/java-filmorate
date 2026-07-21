package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Data
public class NewUserRequestDto {
	private String name;
	@NotNull
	private String login;
	@Email
	@NotNull
	private String email;
	@NotNull
	@PastOrPresent
	private LocalDate birthday;
}