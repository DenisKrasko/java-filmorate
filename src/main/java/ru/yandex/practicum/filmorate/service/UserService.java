package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
	//private List<User> friends;
	// в параметр передаём id пользователя
	public void addFriend(User user, User friend) {
		if (user.getFriends().contains(friend.getId())) {
			System.out.println("Ошибка. Этот пользователь уже ваш друг");
		} else {
			user.addFriend(friend);
		}
	}

	public User delFriend(User user, User friend){
		if (!user.getFriends().contains(friend.getId())) {
			System.out.println("Ошибка. Такого пользователя нету у вас в друзьях");
		} else {
			user.delFriend(friend);
		}
		return user;
	}

	public List<User> getSharedFriends(User user1, User user2){
			List<User> sharedFriends = new ArrayList<>();
			for (Long id : user1.getFriends()) {
				if (user2.getFriends().contains(id)) {
					sharedFriends.add(user1	);
				}
			}
		return friends;
	}
}
