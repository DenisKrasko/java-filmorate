package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.NewUserRequestDto;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequestDto;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
	private final UserService userService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserDto createUser(@Valid @RequestBody NewUserRequestDto userRequest) {
		return userService.createUser(userRequest);
	}

	@DeleteMapping("/{id}/friends/{friendId}")
	public void delFriend(@PathVariable("id") long id,
						  @PathVariable("friendId") long friendId) {
		userService.delFriend(id, friendId);
	}

	@ResponseStatus(HttpStatus.OK)
	@PutMapping("/{id}/friends/{friendId}")
	public UserDto addFriend(@PathVariable("id") long id,
							 @PathVariable("friendId") long friendId) {
		return userService.addFriend(id, friendId);
	}

	@GetMapping("/{id}/friends")
	public List<UserDto> getFriends(@PathVariable("id") long id) {
		return userService.getFriends(id);
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<UserDto> getUsers() {
		return userService.getUsers();
	}

	@GetMapping("/{userId}")
	@ResponseStatus(HttpStatus.OK)
	public UserDto getUserById(@PathVariable("userId") long userId) {
		return userService.getUserById(userId);
	}

	@PutMapping
	public UserDto updateUser(@RequestBody UpdateUserRequestDto request) {
		return userService.updateUser(request);
	}

	@PutMapping("{userId}")
	public UserDto updateUser(@PathVariable("userId") long userId, @RequestBody UpdateUserRequestDto request) {
		return userService.updateUser(userId, request);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{id}")
	public UserDto getUser(@PathVariable("id") long id) {
		return userService.findUserById(id);
	}

	@GetMapping("/{id}/friends/common/{otherId}")
	public List<UserDto> getSharedFriends(@PathVariable("id") long id,
									   @PathVariable("otherId") long otherId) {
		return userService.getSharedFriends(id, otherId);
	}

	public UserService getUserService() {
		return userService;
	}
}