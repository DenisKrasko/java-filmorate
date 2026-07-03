//package ru.yandex.practicum.filmorate;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import ru.yandex.practicum.filmorate.controller.FilmController;
//import ru.yandex.practicum.filmorate.controller.UserController;
//import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
//import ru.yandex.practicum.filmorate.model.Film;
//import ru.yandex.practicum.filmorate.model.User;
//
//import java.time.LocalDate;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class FilmorateApplicationTests {
//
//	@Test
//	void contextLoads() {
//	}
//
//	@Test
//	void emptyMapIsReturnedWhenNoFilmsAdded() {
//		FilmController filmController = new FilmController();
//		Collection<Film> films = filmController.findAll();
//		Map<Long, Film> filmsExp = new HashMap<>();
//		assertIterableEquals(filmsExp.values(), films);
//	}
//
//	@Test
//	void createShouldAddExactlyOneFilm() {
//		FilmController filmController = new FilmController();
//		Film film = new Film();
//		film.setName("Batman");
//		film.setDuration(218);
//		film.setReleaseDate(LocalDate.of(1999,3,25));
//		film.setDescription("adventures Batman and Robin");
//		filmController.create(film);
//		Map<Long, Film> filmsExp = new HashMap<>();
//		filmsExp.put((long) 1, film);
//		assertIterableEquals(filmsExp.values(), filmController.getFilms().values(), "Error");
//	}
//
//	@Test
//	void filmTitleMustNotBeEmpty() {
//		FilmController filmController = new FilmController();
//		Film film = new Film();
//		film.setName("");
//		film.setDuration(218);
//		film.setReleaseDate(LocalDate.of(1999,3,25));
//		film.setDescription("adventures Batman and Robin");
//		assertThrows(ConditionsNotMetException.class, () -> filmController.create(film), "Error");
//	}
//
//	@Test
//	void descriptionOver200CharactersIsInvalid() {
//		FilmController filmController = new FilmController();
//		Film film = new Film();
//		film.setName("Batman");
//		film.setDuration(218);
//		film.setReleaseDate(LocalDate.of(1999,3,25));
//		film.setDescription("A".repeat(201));
//		assertThrows(ConditionsNotMetException.class, () -> filmController.create(film), "Error");
//	}
//
//	@Test
//	void releaseDateBefore28December1895IsInvalid() {
//		FilmController filmController = new FilmController();
//		Film film = new Film();
//		film.setName("Batman");
//		film.setDuration(218);
//		film.setReleaseDate(LocalDate.of(1895,3,25));
//		film.setDescription("adventures Batman and Robin");
//		assertThrows(ConditionsNotMetException.class, () -> filmController.create(film), "Error");
//	}
//
//	@Test
//	void negativeDurationIsInvalid() {
//		FilmController filmController = new FilmController();
//		Film film = new Film();
//		film.setName("Batman");
//		film.setDuration(-218);
//		film.setReleaseDate(LocalDate.of(1999,3,25));
//		film.setDescription("adventures Batman and Robin");
//		assertThrows(ConditionsNotMetException.class, () -> filmController.create(film), "Error");
//	}
//
//	@Test
//	void validUserShouldBeAddedSuccessfully() {
//		User user = new User();
//		user.setName("Dima");
//		user.setLogin("dima99");
//		user.setEmail("dima99@gmail.com");
//		user.setBirthday(LocalDate.of(2001,3,25));
//		UserController userController = new UserController();
//		userController.create(user);
//		Map<Long, User> usersExp = new HashMap<>();
//		usersExp.put((long) 1, user);
//		assertIterableEquals(usersExp.values(), userController.getUsers().values(), "Error");
//	}
//
//	@Test
//	void emptyEmailIsInvalid() {
//		User user = new User();
//		user.setName("Dima");
//		user.setLogin("dima99");
//		user.setEmail("");
//		user.setBirthday(LocalDate.of(2001,3,25));
//		UserController userController = new UserController();
//		assertThrows(ConditionsNotMetException.class, () -> userController.create(user), "Error");
//	}
//
//	@Test
//	void emailMustContainAtSign() {
//		User user = new User();
//		user.setName("Dima");
//		user.setLogin("dima99");
//		user.setEmail("asdsadcom");
//		user.setBirthday(LocalDate.of(2001,3,25));
//		UserController userController = new UserController();
//		assertThrows(ConditionsNotMetException.class, () -> userController.create(user), "Error");
//	}
//
//	@Test
//	void loginMustNotBeEmpty() {
//		User user = new User();
//		user.setName("Dima");
//		user.setLogin("");
//		user.setEmail("asdsad@com");
//		user.setBirthday(LocalDate.of(2001,3,25));
//		UserController userController = new UserController();
//		assertThrows(ConditionsNotMetException.class, () -> userController.create(user), "Error");
//	}
//
//	@Test
//	void loginMustNotContainSpaces() {
//		User user = new User();
//		user.setName("Dima");
//		user.setLogin("di ma");
//		user.setEmail("asdsad@com");
//		user.setBirthday(LocalDate.of(1999,3,25));
//		UserController userController = new UserController();
//		assertThrows(ConditionsNotMetException.class, () -> userController.create(user), "Error");
//	}
//
//	@Test
//	void birthdayMustNotBeInFuture() {
//		User user = new User();
//		user.setName("Dima");
//		user.setLogin("dima99");
//		user.setEmail("asdsad@com");
//		user.setBirthday(LocalDate.of(2028,3,25));
//		UserController userController = new UserController();
//		assertThrows(ConditionsNotMetException.class, () -> userController.create(user), "Error");
//	}
//
//	@Test
//	void emptyDisplayNameShouldFallbackToLogin() {
//		User user = new User();
//		user.setName("");
//		user.setLogin("dima99");
//		user.setEmail("asdsad@com");
//		user.setBirthday(LocalDate.of(2001,3,25));
//		User userExp = new User();
//		userExp.setName("dima99");
//		userExp.setLogin("dima99");
//		userExp.setEmail("asdsad@com");
//		userExp.setBirthday(LocalDate.of(2001,3,25));
//		UserController userController = new UserController();
//		userController.create(user);
//		assertEquals(userExp, userController.getUsers().get((long) 1), "Error");
//	}
//}
