package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserStorage {
	void delFriendLink(Long userId, Long friendId);

	List<User> getFriends(long userId);

	void loadFriends(User user);

	void addFriendLink(Long id, Long friendId);

	Collection<User> findAll(); //++++

	User create(User user); //++++

	Optional<User> findByEmail(String email); //++++

	Optional<User> findById(long userId); //++++

	User update(User newUser); //+++

	Map<Long, User> getUsers();

	User save(User user);

}