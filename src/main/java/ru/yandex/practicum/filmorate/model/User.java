package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.Set;

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
	private Set<Long> friends;

	public void addFriend(User friend) {
		friends.add(friend.getId());
	}

	public void delFriend(User friend) {
		friends.remove(friend.getId());
	}
}