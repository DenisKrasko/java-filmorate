package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public abstract class InMemoryUserStorage implements UserStorage {
	@Getter
	private final Map<Long, User> users;
	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Override
	public Collection<User> findAll() {
		return users.values();
	}

	@Override
	public User create(User user) {
		return saveUser(user);
	}

	@Override
	public User update(User newUser) {
		return updateUser(newUser);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		for (User user : users.values()) {
			if (user.getEmail().equalsIgnoreCase(email)) {
				return Optional.of(user);
			}
		}
		throw new NotFoundException("Пользователь с email " + email + " не найден");
	}

	@Override
	public Optional<User> findById(long id) {
		if (!users.containsKey(id)) {
			throw new NotFoundException("Пользователя с id " + id + " не найдено");
		}
		return Optional.of(users.get(id));
	}

	private User updateUser(User newUser) {
		log.error("проверка выполнения необходимых условий при эндпоинте Put");
		validateUser(newUser);
		if (newUser.getId() == 0) {
			logAndThrow("Id должен быть указан");
			throw new ConditionsNotMetException("Id должен быть указан");
		}
		if (users.containsKey(newUser.getId())) {
			User oldUser = users.get(newUser.getId());
			log.error("пользователь найден и все условия соблюдены, обновляем его содержимое");
			oldUser.setLogin(newUser.getLogin());
			oldUser.setEmail(newUser.getEmail());
			if (oldUser.getName().isBlank()) {
				oldUser.setName(oldUser.getLogin());
			} else {
				oldUser.setName(newUser.getName());
			}
			oldUser.setBirthday(newUser.getBirthday());
			return oldUser;
		}
		log.error("User с id = {} не найден", newUser.getId());
		throw new NotFoundException("User с id = " + newUser.getId() + " не найден");
	}

	private User saveUser(User user) {
		log.trace("проверка выполнения необходимых условий при эндпоинте Post");
		validateUser(user);
		log.trace("формирование дополнительных данных");
		user.setId(getNextId());
		log.trace("сохранение нового пользователя в памяти приложения");
		users.put(user.getId(), user);
		return users.get(user.getId());
	}

	private long getNextId() {
		long currentMaxId = users.keySet()
				.stream()
				.mapToLong(id -> id)
				.max()
				.orElse(0);
		return ++currentMaxId;
	}

	private void logAndThrow(String message) {
		log.error(message);
		throw new ValidationException(message);
	}

	private void validateUser(User user) {
		if (user.getEmail() == null || user.getEmail().isBlank()) {
			logAndThrow("Имейл должен быть указан");
		}
		if (!user.getEmail().contains("@")) {
			logAndThrow("электронная почта должна содержать символ @");
		}
		if (user.getLogin() == null || user.getLogin().isBlank()) {
			logAndThrow("логин не может быть пустым");
		}
		if (user.getLogin().contains(" ")) {
			logAndThrow("логин не может содержать пробелы");
		}
		if (user.getBirthday().isAfter(LocalDate.now())) {
			logAndThrow("дата рождения не может быть в будущем");
		}
		if (user.getName() == null || user.getName().isBlank()) {
			user.setName(user.getLogin());
		}
	}

	@Override
	public Map<Long, User> getUsers() {
		return users;
	}

	@Override
	public User save(User user) {
		return null;
	}

	@Override
	public void loadFriends(User user) {
	}
}