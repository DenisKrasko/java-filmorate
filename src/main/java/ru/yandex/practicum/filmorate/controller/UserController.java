package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	private final UserService userService;

	@GetMapping
	public Collection<User> findAll() {
		return userService.findAll();
	}

	@PostMapping
	public User create(@Valid @RequestBody User user) {
		return userService.create(user);
	}

	@PutMapping
	public User update(@Valid @RequestBody User newUser) {
		return userService.update(newUser);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{id}")
	public User getUser(@PathVariable("id") long id) {
		return userService.findUserById(id);
	}

	@ResponseStatus(HttpStatus.OK)
	@PutMapping("/{id}/friends/{friendId}")
	public void addFriend(@PathVariable("id") long id,
						  @PathVariable("friendId") long friendId) {
		userService.addFriend(id, friendId);
	}

	@DeleteMapping("/{id}/friends/{friendId}")
	public void delFriend(@PathVariable("id") long id,
						  @PathVariable("friendId") long friendId) {
		userService.delFriend(id, friendId);
	}

	@GetMapping("/{id}/friends")
	public List<User> getFriends(@PathVariable("id") long id) {
		return userService.getFriends(id);
	}

	@GetMapping("/{id}/friends/common/{otherId}")
	public List<User> getSharedFriends(@PathVariable("id") long id,
									   @PathVariable("otherId") long otherId) {
		return userService.getSharedFriends(id, otherId);
	}

	public UserService getUserService() {
		return userService;
	}
}