//package ru.yandex.practicum.filmorate;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import ru.yandex.practicum.filmorate.controller.FilmController;
//import ru.yandex.practicum.filmorate.controller.UserController;
//import ru.yandex.practicum.filmorate.dto.NewUserRequest;
//import ru.yandex.practicum.filmorate.dto.UserDto;
//import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
//import ru.yandex.practicum.filmorate.exception.ValidationException;
//import ru.yandex.practicum.filmorate.model.Film;
//import ru.yandex.practicum.filmorate.model.User;
//import ru.yandex.practicum.filmorate.service.InstanceFilmService;
//import ru.yandex.practicum.filmorate.service.InstanceUserService;
//import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
//import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
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
//		FilmController filmController = new FilmController(new InstanceFilmService(new InMemoryFilmStorage(), new InMemoryUserStorage(new HashMap<Long, User>())));
//		Collection<Film> films = filmController.findAll();
//		Map<Long, Film> filmsExp = new HashMap<>();
//		assertIterableEquals(filmsExp.values(), films);
//	}
//
//	@Test
//	void createShouldAddExactlyOneFilm() {
//		FilmController filmController = new FilmController(new InstanceFilmService(new InMemoryFilmStorage(), new InMemoryUserStorage(new HashMap<Long, User>())));
//		Film film = new Film();
//		film.setName("Batman");
//		film.setDuration(218);
//		film.setReleaseDate(LocalDate.of(1999,3,25));
//		film.setDescription("adventures Batman and Robin");
//		filmController.create(film);
//		Map<Long, Film> filmsExp = new HashMap<>();
//		filmsExp.put((long) 1, film);
//		assertIterableEquals(filmsExp.values(), filmController.getFilmService().getFilmStorage().getFilms().values(), "Error");
//	}
//
//	@Test
//	void filmTitleMustNotBeEmpty() {
//		FilmController filmController = new FilmController(new InstanceFilmService(new InMemoryFilmStorage(), new InMemoryUserStorage(new HashMap<Long, User>())));
//		Film film = new Film();
//		film.setName("");
//		film.setDuration(218);
//		film.setReleaseDate(LocalDate.of(1999,3,25));
//		film.setDescription("adventures Batman and Robin");
//		assertThrows(ValidationException.class, () -> filmController.create(film), "Error");
//	}
//
//	@Test
//	void descriptionOver200CharactersIsInvalid() {
//		FilmController filmController = new FilmController(new InstanceFilmService(new InMemoryFilmStorage(), new InMemoryUserStorage(new HashMap<Long, User>())));
//		Film film = new Film();
//		film.setName("Batman");
//		film.setDuration(218);
//		film.setReleaseDate(LocalDate.of(1999,3,25));
//		film.setDescription("A".repeat(201));
//		assertThrows(ValidationException.class, () -> filmController.create(film), "Error");
//	}
//
//	@Test
//	void releaseDateBefore28December1895IsInvalid() {
//		FilmController filmController = new FilmController(new InstanceFilmService(new InMemoryFilmStorage(), new InMemoryUserStorage(new HashMap<Long, User>())));
//		Film film = new Film();
//		film.setName("Batman");
//		film.setDuration(218);
//		film.setReleaseDate(LocalDate.of(1895,3,25));
//		film.setDescription("adventures Batman and Robin");
//		assertThrows(ValidationException.class, () -> filmController.create(film), "Error");
//	}
//
//	@Test
//	void negativeDurationIsInvalid() {
//		FilmController filmController = new FilmController(new InstanceFilmService(new InMemoryFilmStorage(), new InMemoryUserStorage(new HashMap<Long, User>())));
//		Film film = new Film();
//		film.setName("Batman");
//		film.setDuration(-218);
//		film.setReleaseDate(LocalDate.of(1999,3,25));
//		film.setDescription("adventures Batman and Robin");
//		assertThrows(ValidationException.class, () -> filmController.create(film), "Error");
//	}
//
//	@Test
//	void validUserShouldBeAddedSuccessfully() {
//		NewUserRequest request = new NewUserRequest();
//		request.setName("Dima");
//		request.setLogin("dima99");
//		request.setEmail("dima99@gmail.com");
//		request.setBirthday(LocalDate.of(2001, 3, 25));
//		UserController userController = new UserController(new InstanceUserService(new InMemoryUserStorage(new HashMap<Long, User>())));
//		UserDto response = userController.createUser(request);
//		assertNotNull(response);
//		assertEquals("Dima", response.getName());
//		assertEquals("dima99", response.getLogin());
//	}
//
//	@Test
//	void emptyEmailIsInvalid() {
//		NewUserRequest request = new NewUserRequest();
//		request.setName("Dima");
//		request.setLogin("dima99");
//		request.setEmail(""); // Пустой имейл для проверки ошибки
//		request.setBirthday(LocalDate.of(2001, 3, 25));
//
//		UserController userController = new UserController(new InstanceUserService(userStorage));
//
//		// Проверяем, что при пустом имейле выбросится исключение
//		assertThrows(ConditionsNotMetException.class, () -> userController.createUser(request));
//	}
//
//	@Test
//	void emailMustContainAtSign() {
//		User user = new User();
//		user.setName("Dima");
//		user.setLogin("dima99");
//		user.setEmail("asdsadcom");
//		user.setBirthday(LocalDate.of(2001,3,25));
//		UserController userController = new UserController(new InstanceUserService(new InMemoryUserStorage(new HashMap<Long, User>())));
//		assertThrows(ValidationException.class, () -> userController.create(user), "Error");
//	}
//
//	@Test
//	void loginMustNotBeEmpty() {
//		User user = new User();
//		user.setName("Dima");
//		user.setLogin("");
//		user.setEmail("asdsad@com");
//		user.setBirthday(LocalDate.of(2001,3,25));
//		UserController userController = new UserController(new InstanceUserService(new InMemoryUserStorage(new HashMap<Long, User>())));
//		assertThrows(ValidationException.class, () -> userController.create(user), "Error");
//	}
//
//	@Test
//	void loginMustNotContainSpaces() {
//		User user = new User();
//		user.setName("Dima");
//		user.setLogin("di ma");
//		user.setEmail("asdsad@com");
//		user.setBirthday(LocalDate.of(1999,3,25));
//		UserController userController = new UserController(new InstanceUserService(new InMemoryUserStorage(new HashMap<Long, User>())));
//		assertThrows(ValidationException.class, () -> userController.create(user), "Error");
//	}
//
//	@Test
//	void birthdayMustNotBeInFuture() {
//		User user = new User();
//		user.setName("Dima");
//		user.setLogin("dima99");
//		user.setEmail("asdsad@com");
//		user.setBirthday(LocalDate.of(2028,3,25));
//		UserController userController = new UserController(new InstanceUserService(new InMemoryUserStorage(new HashMap<Long, User>())));
//		assertThrows(ValidationException.class, () -> userController.create(user), "Error");
//	}
//
//	@Test
//	void emptyDisplayNameShouldFallbackToLogin() {
//
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
//		UserController userController = new UserController(new InstanceUserService(new InMemoryUserStorage(new HashMap<Long, User>())));
//		userController.createUser(user);
//		assertEquals(userExp, userController.getUserService().getUserStorage().getUsers().get((long) 1), "Error");
//	}
//}
