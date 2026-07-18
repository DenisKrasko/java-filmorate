package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class InstanceUserService implements UserService {
	private final UserStorage userStorage;

	public InstanceUserService(@Qualifier("userDbStorage") UserStorage userStorage) {
		this.userStorage = userStorage;
	}

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
		Map<Long, User> allUsers = userStorage.getUsers();
		User user1 = allUsers.get(id);
		User user2 = allUsers.get(otherId);
		if (user1 == null || user2 == null) {
			throw new NotFoundException("Пользователь не найден");
		}
		List<User> sharedFriends = new ArrayList<>();
		for (Long friendId : user1.getFriends()) {
			if (user2.getFriends().contains(friendId)) {
				sharedFriends.add(allUsers.get(friendId));
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
		return userStorage.findById(id).orElseThrow(() -> new NotFoundException("Пользователь c id " + id + " не найден"));
	}

	@Override
	public UserStorage getUserStorage() {
		return userStorage;
	}
}
