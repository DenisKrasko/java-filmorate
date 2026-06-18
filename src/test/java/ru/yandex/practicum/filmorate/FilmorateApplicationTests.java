package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.http.HttpClient;

@SpringBootTest
class FilmorateApplicationTests {
	private static final String BASE = "http://localhost:8080/movies";
	private static final String JSON_CONTENT_TYPE = "application/json; charset=UTF-8";
//	private static MoviesServer server;
//	private static HttpClient client;
//	private static MoviesStore moviesStore = new MoviesStore();
//	private Gson gson = new Gson();

	@Test
	void contextLoads() {
	}

}
