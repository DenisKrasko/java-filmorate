package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserDto {
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long id;
	private String name;
	private String email;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String login;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private LocalDate birthday;
	private List<Long> friends = new ArrayList<>();
}