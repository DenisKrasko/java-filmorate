package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
//@RequiredArgsConstructor
@Primary
public class UserDbStorage extends BaseRepository<User> implements UserStorage {

//	protected final JdbcTemplate jdbc;
//	protected final RowMapper<T> mapper;
	private static final String FIND_ALL_QUERY = "SELECT * FROM users";
	private static final String FIND_BY_EMAIL_QUERY = "SELECT * FROM users WHERE email = ?";
	private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";

	private static final String INSERT_QUERY = "INSERT INTO users (email, login, username, birthday) VALUES (?, ?, ?, ?)";
	private static final String UPDATE_QUERY = "UPDATE users SET username = ?, login = ?, birthday = ? WHERE id = ?";

	private static final String ADD_FRIEND_QUERY = "INSERT INTO friends (user_id, friend_id, status) VALUES (?, ?, ?)";
	private static final String FIND_FRIENDS = "SELECT friend_id FROM friends WHERE user_id = ?";

	private static final String CREATE_QUERY = "INSERT INTO users(email, login, username, birthday) VALUES (?, ?, ?, ?)";

	public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
		super(jdbc, mapper);
	}

	@Override
	public List<User> findAll() {
		List<User> users = findMany(FIND_ALL_QUERY);
		users.forEach(this::loadFriends);

//		String query = "SELECT * FROM users";
//		return jdbc.query(query, mapper);
		return findMany(FIND_ALL_QUERY);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return findOne(FIND_BY_EMAIL_QUERY, email);
//		Optional<User> userOptional = findOne(FIND_BY_EMAIL_QUERY, email);
//		return loadFriendsIfPresent(userOptional);
	}

	@Override
	public Optional<User> findById(long userId) {
		return findOne(FIND_BY_ID_QUERY, userId);
//		Optional<User> userOptional = findOne(FIND_BY_ID_QUERY, userId);
//		return loadFriendsIfPresent(userOptional);
	}
//	public Optional<User> findByEmail(String email) {
//		String query = "SELECT * FROM users WHERE email = ?";
//		try {
//			User result = jdbc.queryForObject(query, mapper, email);
//			return Optional.ofNullable(result);
//		} catch (EmptyResultDataAccessException ignored) {
//			return Optional.empty();
//		}
//	}

	@Override
	public User save(User user) {
		long id = insert(
				INSERT_QUERY,
				user.getEmail(),
				user.getLogin(),
				user.getName(),
				user.getBirthday()
//				Timestamp.from(user.getBirthday())
//				java.sql.Date.valueOf(user.getBirthday())
		);
		user.setId(id);
		return user;
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
//				Date.valueOf(user.getBirthday())
		);
		user.setId(id);
		return user;
	}

	private static final String CHECK_REVERSE_LINK =
			"SELECT COUNT(*) FROM friends WHERE user_id = ? AND friend_id = ?";
	private static final String UPDATE_STATUS_QUERY =
			"UPDATE friends SET statu = true WHERE user_id = ? AND friend_id = ?";

//	public void addFriendLink(long id, long friendId) {
//		// 1. Проверяем, отправлял ли уже friendId заявку к id
//		Integer count = jdbc.queryForObject(CHECK_REVERSE_LINK, Integer.class, friendId, id);
//
//		if (count != null && count > 0) {
//			// Если обратная заявка есть — делаем дружбу взаимной (status = true)
//			jdbc.update(UPDATE_STATUS_QUERY, friendId, id); // обновляем старую заявку на true
//			jdbc.update(ADD_FRIEND_QUERY, id, friendId, true); // создаем новую сразу с true
//		} else {
//			// Если обратной заявки нет — это первая односторонняя заявка (status = false)
//			jdbc.update(ADD_FRIEND_QUERY, id, friendId, false);
//		}
//	}

	public void loadFriends(User user) {
		List<Long> friendIds = jdbc.queryForList(FIND_FRIENDS, Long.class, user.getId());
//		List<Long> friendIds = jdbc.queryForList(FIND_FRIENDS, Long.class, user.getId());
		user.setFriends(friendIds);
	}

	public void addFriendLink(long id, long friendId) {
//		jdbc.update(ADD_FRIEND_QUERY, id, friendId, true);
//		jdbc.update(ADD_FRIEND_QUERY, friendId, id, true);
		jdbc.update(ADD_FRIEND_QUERY, id, friendId, false);
	}





//	public Optional<User> findById(long userId) {
//		String query = "SELECT * FROM users WHERE id = ?";
//		try {
//			User result = jdbc.queryForObject(query, mapper, userId);
//			return Optional.ofNullable(result);
//		} catch (EmptyResultDataAccessException ignored) {
//			return Optional.empty();
//		}
//	}





	@Override
	public Map<Long, User> getUsers() {
		Collection<User> allUsers = findAll();
		return allUsers.stream().collect(Collectors.toMap(User::getId, user -> user));
	}

//	private void loadFriends(User user) {
//		List<Long> friendIds = jdbc.query(FIND_FRIENDS, (rs, rowNum) -> rs.getLong("friend_id"), user.getId());
//		user.setFriends(friendIds);
//	}



	private Optional<User> loadFriendsIfPresent(Optional<User> userOptional) {
		User user = userOptional.orElseThrow(() -> new NotFoundException("Пользователь не найден"));
		loadFriends(user);
		return userOptional;
	}
}