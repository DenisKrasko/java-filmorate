package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InstanceUserService implements UserService {
	private final UserStorage userStorage;

	@Override
	public void addFriend(long id, long friendId) {
		if (!userStorage.getUsers().containsKey(id)) {
			throw new NotFoundException("Пользователь с id = " + id + ", для которого нужно добавить друга, не найден");
		}
		if (!userStorage.getUsers().containsKey(friendId)) {
			throw new NotFoundException("Пользователь с id = " + friendId + ", которого вы хотите добавить в друзья, не найден");
		}
		User user = userStorage.getUsers().get(id);
		User friend = userStorage.getUsers().get(friendId);
		user.addFriend(friendId);
		friend.addFriend(id);
	}

	@Override
	public void delFriend(long id, long friendId) {
		if (!userStorage.getUsers().containsKey(id)) {
			throw new NotFoundException("Пользователь с id = " + id + ", для которого нужно добавить друга, не найден");
		}
		if (!userStorage.getUsers().containsKey(friendId)) {
			throw new NotFoundException("Пользователь с id = " + friendId + ", которого вы хотите добавить в друзья, не найден");
		}
		User user = userStorage.getUsers().get(id);
		User friend = userStorage.getUsers().get(friendId);
		user.delFriend(friendId);
		friend.delFriend(id);
	}

	@Override
	public List<User> getFriends(long id) {
		if (!userStorage.getUsers().containsKey(id)) {
			throw new NotFoundException("Пользователь с id = " + id + ", друзей которого нужно получить, не найден");
		}
		ArrayList<User> arrayList = new ArrayList<>();
		User user = userStorage.getUsers().get(id);
		for (long id2 : user.getFriends()) {
			arrayList.add(userStorage.getUsers().get(id2));
		}
		return arrayList;
	}

	@Override
	public List<User> getSharedFriends(long id, long otherId) {
		List<User> sharedFriends = new ArrayList<>();
		User user1 = userStorage.getUsers().get(id);
		User user2 = userStorage.getUsers().get(otherId);
		for (Long id2 : user1.getFriends()) {
			if (user2.getFriends().contains(id2)) {
				sharedFriends.add(userStorage.findUserById(id2));
			}
		}
		return sharedFriends;
	}

	@Override
	public Collection<User> findAll() {
		return userStorage.findAll();
	}

	@Override
	public User create(User user) {
		return userStorage.create(user);
	}

	@Override
	public User update(User newUser) {
		return userStorage.update(newUser);
	}

	@Override
	public User findUserById(Long id) {
		return userStorage.findUserById(id);
	}

	@Override
	public UserStorage getUserStorage() {
		return userStorage;
	}
}
