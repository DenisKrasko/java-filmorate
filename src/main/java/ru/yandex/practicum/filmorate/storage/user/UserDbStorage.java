package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserDbStorage extends BaseRepository<User> implements UserStorage {

	private static final String FIND_ALL_QUERY = "SELECT * FROM users";
	private static final String FIND_BY_EMAIL_QUERY = "SELECT * FROM users WHERE email = ?";
	private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
	private static final String INSERT_QUERY = "INSERT INTO users(username, email, password, registration_date) " +
			"VALUES (?, ?, ?, ?) returning id";
	private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, username = ?, birthday = ? " +
			"WHERE id = ?";
	private static final String FIND_FRIENDS = "SELECT friend_id FROM friends WHERE user_id = ?";
	private static final String CREATE_QUERY = "INSERT INTO user (email, login, name, birthday) VALUES (?, ?, ?, ?)";

	public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
		super(jdbc, mapper, User.class);
	}

	public List<User> findAll() {
		List<User> users = findMany(FIND_ALL_QUERY);
		users.forEach(this::loadFrinds);
		return users;
	}

	public User create(User user) {
		long id = insert(
				CREATE_QUERY,
				user.getEmail(),
				user.getLogin(),
				user.getUsername(),
				Date.valueOf(user.getBirthday())
		);
		user.setId(id);
		return user;
	}

	public Optional<User> findByEmail(String email) {
		Optional<User> userOptional = findOne(FIND_BY_EMAIL_QUERY, email);
		return loadFriendsIfPresent(userOptional);
	}

	public Optional<User> findById(long userId) {
		Optional<User> userOptional = findOne(FIND_BY_ID_QUERY, userId);
		return loadFriendsIfPresent(userOptional);
	}

	public User save(User user) {
		long id = insert(
				INSERT_QUERY,
				user.getLogin(),
				user.getUsername(),
				user.getEmail(),
				Date.valueOf(user.getBirthday())
		);
		user.setId(id);
		return user;
	}

	@Override
	public User update(User user) {
		boolean updated = update(
				UPDATE_QUERY,
				user.getEmail(),
				user.getLogin(),
				user.getUsername(),
				java.sql.Date.valueOf(user.getBirthday()),
				user.getId()
		);
		if (!updated) {
			throw new NotFoundException("Пользователь с id " + user.getId() + " не найден");
		}
		return user;
	}

	@Override
	public Map<Long, User> getUsers() {
		Collection<User> allUsers = findAll();
		return allUsers.stream().collect(Collectors.toMap(User::getId, user -> user));
	}

	private void loadFrinds(User user) {
		List<Long> friendIds = jdbc.queryForList(FIND_FRIENDS, Long.class, user.getId());
		user.setFriends(friendIds);
	}

	private Optional<User> loadFriendsIfPresent(Optional<User> userOptional) {
		userOptional.ifPresent(this::loadFrinds);
		return userOptional;
	}
}
