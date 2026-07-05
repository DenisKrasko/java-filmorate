package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
	@EqualsAndHashCode.Exclude
	private Long id;
	private String email;
	private String login;
	private String name;
	@JsonFormat
	private LocalDate birthday;
	private Set<Long> friends = new HashSet<>();

	public void addFriend(long id) {
		friends.add(id);
		System.out.println(friends);
	}

	public void delFriend(long friendId) {
		friends.remove(friendId);
	}
}