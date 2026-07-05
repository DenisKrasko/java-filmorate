package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
	private final UserStorage userStorage;
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	private final UserService userService;

	@GetMapping
	public Collection<User> findAll() {
		return userStorage.findAll();
	}

	@PostMapping
	public User create(@Valid @RequestBody User user) {
		return userStorage.create(user);
	}

	@PutMapping
	public User update(@Valid @RequestBody User newUser) {
		return userStorage.update(newUser);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{id}")
	public User getUser(@PathVariable("id") long id) {
//		return userStorage.findUserById(id);
//		Optional<User> userOptional = userStorage.findUserById(id);
//		if (userOptional.isEmpty()) {
//			throw new ConditionsNotMetException("Пользователь с id = " + post.getAuthorId() + " не найден")
//		}
//		return userOptional.get();
		System.out.println(userStorage);
		return userStorage.findUserById(id);
	}

	@ResponseStatus(HttpStatus.OK)
	@PutMapping("/{id}/friends/{friendId}")
	public void addFriend(@PathVariable("id") long id,
							   @PathVariable("friendId") long friendId) {
		userService.addFriend(id, friendId);
	}

//	@ResponseStatus(HttpStatus.OK)
//	@GetMapping("/{id}/friends")
//	public Set<Long> getFriends(@PathVariable("id") long id) {
//		return userService.getFriends(id);
//	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{id}/friends")
	public List<User> getFriends(@PathVariable("id") long id) {
		return userService.getFriends(id);
	}

	@DeleteMapping("/{id}/friends/{friendId}")
	public void delFriend(@PathVariable("id") long id,
						  @PathVariable("friendId") long friendId) {
		userService.delFriend(id, friendId);
	}
}