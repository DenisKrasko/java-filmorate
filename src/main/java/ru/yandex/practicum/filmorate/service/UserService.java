package ru.yandex.practicum.filmorate.service;

import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;


public interface UserService {
	UserDto addFriend(long id, long friendId);

	void delFriend(long id, long friendId);

	List<UserDto> getFriends(long id);

	List<User> getSharedFriends(long id, long otherId);

	Collection<User> findAll();

	UserDto createUser(NewUserRequest userRequest);

	UserDto updateUser(UpdateUserRequest request);

	UserDto updateUser(long userId, UpdateUserRequest request);

	User findUserById(Long id);

	UserStorage getUserStorage();

	List<UserDto> getUsers();

	UserDto getUserById(long userId);
}