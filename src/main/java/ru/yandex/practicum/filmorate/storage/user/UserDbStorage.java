package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
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
@Primary
public class UserDbStorage extends BaseRepository<User> implements UserStorage {

	private static final String FIND_ALL_QUERY = "SELECT * FROM users";
	private static final String FIND_BY_EMAIL_QUERY = "SELECT * FROM users WHERE email = ?";
	private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
	private static final String INSERT_QUERY = "INSERT INTO users (email, login, username, birthday) VALUES (?, ?, ?, ?) returning id";
	private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, username = ?, birthday = ? " +
			"WHERE id = ?";
	private static final String FIND_FRIENDS = "SELECT friend_id FROM friends WHERE user_id = ?";
	private static final String CREATE_QUERY = "INSERT INTO users (email, login, username, birthday) VALUES (?, ?, ?, ?)";

	public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
		super(jdbc, mapper, User.class);
	}

	@Override
	public List<User> findAll() {
		List<User> users = findMany(FIND_ALL_QUERY);
		users.forEach(this::loadFriends);

		return users;
	}

	@Override
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

	@Override
	public Optional<User> findByEmail(String email) {
		Optional<User> userOptional = findOne(FIND_BY_EMAIL_QUERY, email);
		return loadFriendsIfPresent(userOptional);
	}

	@Override
	public Optional<User> findById(long userId) {
		Optional<User> userOptional = findOne(FIND_BY_ID_QUERY, userId);
		return loadFriendsIfPresent(userOptional);
	}

	public User save(User user) {
		long id = insert(
				INSERT_QUERY,
				user.getEmail(),
				user.getLogin(),
				user.getUsername(),
				java.sql.Date.valueOf(user.getBirthday())
		);
		user.setId(id);
		return user;
	}

	@Override
	public User update(User user) {
		update(
				UPDATE_QUERY,
				user.getEmail(),
				user.getLogin(),
				user.getUsername(),
				java.sql.Date.valueOf(user.getBirthday()),
				user.getId()
		);
		return user;
	}

	@Override
	public Map<Long, User> getUsers() {
		Collection<User> allUsers = findAll();
		return allUsers.stream().collect(Collectors.toMap(User::getId, user -> user));
	}

	private void loadFriends(User user) {
		List<Long> friendIds = jdbc.queryForList(FIND_FRIENDS, Long.class, user.getId());
		user.setFriends(friendIds);
	}

	private Optional<User> loadFriendsIfPresent(Optional<User> userOptional) {
		User user = userOptional.orElseThrow(() -> new NotFoundException("Пользователь не найден"));
		loadFriends(user);
		return userOptional;
	}
}