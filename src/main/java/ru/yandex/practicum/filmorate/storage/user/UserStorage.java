package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface UserStorage {
	Collection<User> findAll(); //++++

	User create(User user); //++++

	Optional<User> findByEmail(String email); //++++

	Optional<User> findById(long userId); //++++

	User update(User newUser); //+++

	Map<Long, User> getUsers();

}