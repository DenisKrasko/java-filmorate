package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserService {
	UserDto addFriend(Long id, Long friendId);

	void delFriend(long id, long friendId);

	List<UserDto> getFriends(Long id);

	List<User> getSharedFriends(long id, long otherId);

	Collection<User> findAll();

	UserDto createUser(NewUserRequest userRequest);

	UserDto updateUser(UpdateUserRequest request);

	UserDto updateUser(long userId, UpdateUserRequest request);

	User findUserById(Long id);

	List<UserDto> getUsers();

	UserDto getUserById(long userId);
}