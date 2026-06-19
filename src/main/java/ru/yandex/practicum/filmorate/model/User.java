package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
public class User {
	@EqualsAndHashCode.Exclude
	private Long id;
	@NotBlank
	@Email(message = "Некорректный email")
	private String email;
	@NotBlank(message = "Логин не может быть пустым")
	@Pattern(regexp = "^\\S+$", message = "логин не может содержать пробелы")
	private String login;
	@NotBlank
	private String name;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthday;

}