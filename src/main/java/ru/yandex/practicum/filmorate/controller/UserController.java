package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
	private final Map<Long, User> users = new HashMap<>();
	private final static Logger log = LoggerFactory.getLogger(UserController.class);

	@GetMapping
	public Collection<User> findAll() {
		return users.values();
	}

	@PostMapping
	public User create(@RequestBody User user) {
		log.trace("проверка выполнения необходимых условий при эндпоинте Post");
		validateUser(user);
		log.trace("формирование дополнительных данных");
		user.setId(getNextId());
		log.trace("сохранение нового пользователя в памяти приложения");
		users.put(user.getId(), user);
		return user;
	}

	@PutMapping
	public User update(@RequestBody User newUser) {
		log.trace("проверка выполнения необходимых условий при эндпоинте Put");
		validateUser(newUser);
		if (newUser.getId() == null) {
			logAndThrow("Id должен быть указан");
		}
		if (users.containsKey(newUser.getId())) {
			User oldUser = users.get(newUser.getId());
			log.trace("пользователь найден и все условия соблюдены, обновляем его содержимое");
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

	/** Вспомогательный метод для генерации идентификатора нового поста */
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
		throw new ConditionsNotMetException(message);
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
		if (user.getBirthday().isAfter(LocalDate.now())){
			logAndThrow("дата рождения не может быть в будущем");
		}
		if (user.getName() == null || user.getName().isBlank()) {
			user.setName(user.getLogin());
		}
	}
}
