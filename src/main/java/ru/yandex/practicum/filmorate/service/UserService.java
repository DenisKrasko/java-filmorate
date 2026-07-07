package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;


public interface UserService {
	void addFriend(long id, long friendId);

	void delFriend(long id, long friendId);

	List<User> getFriends(long id);

	List<User> getSharedFriends(long id, long otherId);

	Collection<User> findAll();

	User create(User user);

	User update(User newUser);

	User findUserById(Long id);

	UserStorage getUserStorage();
}