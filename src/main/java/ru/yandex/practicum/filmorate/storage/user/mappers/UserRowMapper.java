package ru.yandex.practicum.filmorate.storage.user.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;


@Component
public class UserRowMapper implements RowMapper<User> {
	@Override
	public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		User user = new User();
		user.setId(resultSet.getLong("id"));
		user.setEmail(resultSet.getString("email"));
		user.setLogin(resultSet.getString("login"));
		user.setName(resultSet.getString("username"));
		Timestamp birthdayTimestamp = resultSet.getTimestamp("birthday");
		user.setBirthday(birthdayTimestamp.toLocalDateTime().toLocalDate());
		return user;
	}
}