package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.NewUserRequestDto;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequestDto;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserService {
	UserDto addFriend(Long id, Long friendId);

	void delFriend(long id, long friendId);

	List<UserDto> getFriends(Long id);

	List<UserDto> getSharedFriends(long id, long otherId);

	Collection<User> findAll();

	UserDto createUser(NewUserRequestDto userRequest);

	UserDto updateUser(UpdateUserRequestDto request);

	UserDto updateUser(long userId, UpdateUserRequestDto request);

	UserDto findUserById(Long id);

	List<UserDto> getUsers();

	UserDto getUserById(long userId);
}