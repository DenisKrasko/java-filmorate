package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
	private final UserService userService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserDto createUser(@Valid @RequestBody NewUserRequest userRequest) {
		return userService.createUser(userRequest);
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<UserDto> getUsers(){
		return userService.getUsers();
	}

	@GetMapping("/{userId}")
	@ResponseStatus(HttpStatus.OK)
	public UserDto getUserById(@PathVariable("userId") long userId) {
		return userService.getUserById(userId);
	}

	@PutMapping
	public UserDto updateUser(@RequestBody UpdateUserRequest request) { // или тот класс запроса обновления, который у вас используется
		return userService.updateUser(request);
	}

	@PutMapping("{userId}")
	public UserDto updateUser(@PathVariable("userId") long userId, @RequestBody UpdateUserRequest request) {
		return userService.updateUser(userId, request);
	}

	@ResponseStatus(HttpStatus.OK)
	@PutMapping("/{id}/friends/{friendId}")
	public UserDto addFriend(@PathVariable("id") long id,
						  @PathVariable("friendId") long friendId) {
		return userService.addFriend(id, friendId);
	}

//	@PostMapping
//	public User create(@Valid @RequestBody User user) {
//		return userService.create(user);
//	}







//	@GetMapping
//	@ResponseStatus
//	public Collection<User> findAll() {
//		return userService.findAll();
//	}




	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{id}")
	public User getUser(@PathVariable("id") long id) {
		return userService.findUserById(id);
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