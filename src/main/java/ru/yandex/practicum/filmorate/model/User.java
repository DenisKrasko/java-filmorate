package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
public class User {
	@EqualsAndHashCode.Exclude
	private Long id;
	private String email;
	private String login;
	private String name;
	private LocalDate birthday;

	public void setBirthday(String dateStr) {
		if (dateStr == null || dateStr.isEmpty()) {
			this.birthday = null;
		} else {
			this.birthday = LocalDate.parse(dateStr);
		}
	}

	public void setBirthday(LocalDate date) {
		this.birthday = date;
	}
}
