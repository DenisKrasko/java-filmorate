package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InstanceUserService implements UserService {
	private final UserStorage userStorage;

	public InstanceUserService(@Qualifier("userDbStorage") UserStorage userStorage) {
		this.userStorage = userStorage;
	}

	@Override
	public void delFriend(long id, long friendId) {
		userStorage.findById(id)
				.orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
		userStorage.findById(friendId)
				.orElseThrow(() -> new NotFoundException("Пользователь с id = " + friendId + " не найден"));
//		if (!userStorage.getUsers().containsKey(id)) {
//			throw new NotFoundException("Пользователь с id = " + id + ", для которого нужно добавить друга, не найден");
//		}
//		if (!userStorage.getUsers().containsKey(friendId)) {
//			throw new NotFoundException("Пользователь с id = " + friendId + ", которого вы хотите добавить в друзья, не найден");
//		}
		userStorage.delFriendLink(id, friendId);
//		User user = userStorage.getUsers().get(id);
//		User friend = userStorage.getUsers().get(friendId);
//		user.delFriend(friendId);
//		friend.delFriend(id);
	}

	@Override
	public List<UserDto> getFriends(Long id) {
		// Проверяем существование пользователя
		userStorage.findById(id)
				.orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));

		return userStorage.getFriends(id).stream()
				.map(UserMapper::mapToUserDto)
				.collect(Collectors.toList());
	}

	@Override
	public UserDto addFriend(Long id, Long friendId) {
		User user = userStorage.findById(id).
				orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + ", для которого нужно добавить друга, не найден"));
		User friend = userStorage.findById(friendId).
				orElseThrow(() -> new NotFoundException("Пользователь с id = " + friendId + ", которого вы хотите добавить в друзья, не найден"));
		userStorage.addFriendLink(id, friendId);
		User updateUser = userStorage.findById(id)
				.orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + "не найден"));
		return UserMapper.mapToUserDto(updateUser);
	}



	@Override
	public UserDto createUser(NewUserRequest request) {
		if (request.getEmail() == null || request.getEmail().isEmpty()) {
			throw new ConditionsNotMetException("Имейл должен быть указан");
		}

		if (request.getLogin() == null || request.getLogin().isBlank() || request.getLogin().contains(" ")) {
			throw new ValidationException("Логин не может быть пустым или содержать пробелы");
		}

		if (!request.getEmail().contains("@")) {
			throw new ValidationException("Имейл должен содержать символ @");
		}

		if (request.getBirthday() != null && request.getBirthday().isAfter(LocalDate.now())) {
			throw new ValidationException("Дата рождения не может быть в будущем");
		}

		Optional<User> alreadyExistUser = userStorage.findByEmail(request.getEmail());
		if (alreadyExistUser.isPresent()) {
			throw new DuplicatedDataException("Данный имейл уже используется");
		}

		User user = UserMapper.mapToUser(request);

		user = userStorage.save(user);

		return UserMapper.mapToUserDto(user);
	}

	public UserDto getUserById(long userId) {
		return userStorage.findById(userId)
				.map(UserMapper::mapToUserDto)
				.orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));
	}

	public List<UserDto> getUsers() {
		return userStorage.findAll()
				.stream()
				.map(UserMapper::mapToUserDto)
				.collect(Collectors.toList());
	}

	public UserDto updateUser(UpdateUserRequest request) {
		User updatedUser = userStorage.findById(request.getId())
				.map(user -> UserMapper.updateUserFields(user, request))
				.orElseThrow(() -> new NotFoundException("Пользователь не найден"));
		updatedUser = userStorage.update(updatedUser);
		return UserMapper.mapToUserDto(updatedUser);
//		return userStorage.update(newUser);
	}

	public UserDto updateUser(long userId, UpdateUserRequest request) {
		User updatedUser = userStorage.findById(userId)
				.map(user -> UserMapper.updateUserFields(user, request))
				.orElseThrow(() -> new NotFoundException("Пользователь не найден"));
		updatedUser = userStorage.update(updatedUser);
		return UserMapper.mapToUserDto(updatedUser);
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
	public User findUserById(Long id) {
		return userStorage.findById(id).orElseThrow(() -> new NotFoundException("Пользователь c id " + id + " не найден"));
	}

	@Override
	public UserStorage getUserStorage() {
		return userStorage;
	}
}
