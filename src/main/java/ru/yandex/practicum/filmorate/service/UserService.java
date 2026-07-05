package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserStorage userStorage;
	//private List<User> friends;
	// в параметр передаём id пользователя
//		if (user.getFriends().contains(friend.getId())) {
//			System.out.println("Ошибка. Этот пользователь уже ваш друг");
//			throw new
//		}
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

	public void delFriend(long id, long friendId) {
		if (!userStorage.getUsers().containsKey(id)) {
			throw new NotFoundException("Пользователь с id = " + id + ", для которого нужно добавить друга, не найден");
		}
		if (!userStorage.getUsers().containsKey(friendId)) {
			throw new NotFoundException("Пользователь с id = " + friendId + ", которого вы хотите добавить в друзья, не найден");
		}
//		if (!userStorage.getUsers().get(id).getFriends().contains(friendId)) {
//			throw new NotFoundException("Пользователя с id = " + friendId + " нету у вас в друзьях");
//		}
		User user = userStorage.getUsers().get(id);
		User friend = userStorage.getUsers().get(friendId);
		user.delFriend(friendId);
		friend.delFriend(id);
	}

//	public Set<Long> getFriends(long id) {
//		if (!userStorage.getUsers().containsKey(id)) {
//			throw new NotFoundException("Пользователь с id = " + id + ", друзей которого нужно получить, не найден");
//		}
//		User user = userStorage.getUsers().get(id);
//		return user.getFriends();
//	}

	public List<User> getFriends(long id) {
		if (!userStorage.getUsers().containsKey(id)) {
			throw new NotFoundException("Пользователь с id = " + id + ", друзей которого нужно получить, не найден");
		}
		User user = userStorage.getUsers().get(id);
		ArrayList<User> arrayList= new ArrayList<>();
		for (long id2 : user.getFriends()){
			arrayList.add(userStorage.getUsers().get(id2));
		}
		return arrayList;
	}

	public List<User> getSharedFriends(User user1, User user2) {
		List<User> sharedFriends = new ArrayList<>();
		for (Long id : user1.getFriends()) {
			if (user2.getFriends().contains(id)) {
				sharedFriends.add(user1);
			}
		}
		return sharedFriends;
	}




}