package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

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
	private static final String INSERT_QUERY = "INSERT INTO users (email, login, username, birthday) VALUES (?, ?, ?, ?)";
	private static final String UPDATE_QUERY = "UPDATE users SET username = ?, login = ?, birthday = ? WHERE id = ?";
	private static final String ADD_FRIEND_QUERY = "INSERT INTO friends (user_id, friend_id, status) VALUES (?, ?, ?)";
	private static final String FIND_FRIENDS = "SELECT friend_id FROM friends WHERE user_id = ?";
	private static final String CHECK_REVERSE_LINK =
			"SELECT COUNT(*) FROM friends WHERE user_id = ? AND friend_id = ?";
	private static final String INSERT_FRIEND =
			"INSERT INTO friends (user_id, friend_id, status) VALUES (?, ?, ?)";
	private static final String UPDATE_STATUS =
			"UPDATE friends SET status = true WHERE user_id = ? AND friend_id = ?";
	private static final String CREATE_QUERY = "INSERT INTO users(email, login, username, birthday) VALUES (?, ?, ?, ?)";
	private static final String CHECK_REVERSE_LINK2 =
			"SELECT COUNT(*) FROM friends WHERE user_id = ? AND friend_id = ?";
	private static final String UPDATE_STATUS_QUERY =
			"UPDATE friends SET statu = true WHERE user_id = ? AND friend_id = ?";
	private static final String FIND_ALL_FRIENDS_OBJECTS = """
			SELECT u.id, u.email, u.login, u.username, u.birthday
			FROM users u
			JOIN friends f ON u.id = f.friend_id
			WHERE f.user_id = ?
			""";
	private static final String DEL_FRIEND_QUERY = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";

	public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
		super(jdbc, mapper);
	}

	@Override
	public Optional<User> findById(long userId) {
		Optional<User> userOptional = findOne(FIND_BY_ID_QUERY, userId);
		userOptional.ifPresent(this::loadFriends);
		return userOptional;
	}

	@Override
	public Optional<User> findByEmail(String email) {
		Optional<User> userOptional = findOne(FIND_BY_EMAIL_QUERY, email);
		userOptional.ifPresent(this::loadFriends);
		return userOptional;
	}

	@Override
	public User save(User user) {
		long id = insert(
				INSERT_QUERY,
				user.getEmail(),
				user.getLogin(),
				user.getName(),
				user.getBirthday()
		);
		user.setId(id);
		return user;
	}

	@Override
	public void delFriendLink(Long userId, Long friendId) {
		jdbc.update(DEL_FRIEND_QUERY, userId, friendId);
	}

	@Override
	public List<User> getFriends(long userId) {
		List<User> friends = findMany(FIND_ALL_FRIENDS_OBJECTS, userId);
		friends.forEach(this::loadFriends);
		return friends;
	}

	public void addFriendLink(Long userId, Long friendId) {
		jdbc.update(INSERT_FRIEND, userId, friendId, false);
	}

	@Override
	public List<User> findAll() {
		List<User> users = findMany(FIND_ALL_QUERY);
		users.forEach(this::loadFriends);
		return users;
	}


	public void loadFriends(User user) {
		List<Long> friendIds = jdbc.queryForList(FIND_FRIENDS, Long.class, user.getId());
		user.setFriends(friendIds);
	}

	@Override
	public User update(User user) {
		update(
				UPDATE_QUERY,
				user.getName(),
				user.getLogin(),
				user.getBirthday(),
				user.getId()
		);
		return user;
	}

	@Override
	public User create(User user) {
		long id = insert(
				INSERT_QUERY,
				user.getEmail(),
				user.getLogin(),
				user.getName(),
				user.getBirthday()
		);
		user.setId(id);
		return user;
	}

	@Override
	public Map<Long, User> getUsers() {
		Collection<User> allUsers = findAll();
		return allUsers.stream().collect(Collectors.toMap(User::getId, user -> user));
	}


	private Optional<User> loadFriendsIfPresent(Optional<User> userOptional) {
		User user = userOptional.orElseThrow(() -> new NotFoundException("Пользователь не найден"));
		loadFriends(user);
		return userOptional;
	}
}