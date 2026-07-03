package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
public class User {
	@EqualsAndHashCode.Exclude
	private Long id;
	@Email
	private String email;
	private String login;
	private String name;
	@JsonFormat
	private LocalDate birthday;
}