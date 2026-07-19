package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class NewUserRequest {
	private String name;
	private String login;
	private String email;
	private LocalDate birthday;
}