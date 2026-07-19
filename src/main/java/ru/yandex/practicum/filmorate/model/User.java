package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(of = { "email" })
public class User {
	private Long id;
	private String email;
	private String login;
	private String name;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthday;
	private List<Long> friends = new ArrayList<>();

	public void addFriend(long id) {
		friends.add(id);
	}

	public void delFriend(long friendId) {
		friends.remove(friendId);
	}
}