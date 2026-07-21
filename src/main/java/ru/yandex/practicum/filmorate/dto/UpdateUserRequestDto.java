package ru.yandex.practicum.filmorate.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
public class UpdateUserRequestDto {
	private Long id;
	@Setter
	private String name;
	@Setter
	private String email;
	private String login;
	@Setter
	private LocalDate birthday;


	public boolean hasUsername() {
		return !(name == null || name.isBlank());
	}

	public boolean hasEmail() {
		return !(email == null || email.isBlank());
	}

	public boolean hasLogin() {
		return !(login == null || login.isBlank());
	}

	public boolean hasBirthday() {
		return !(birthday == null || birthday.isAfter(LocalDate.now()));
	}
}
