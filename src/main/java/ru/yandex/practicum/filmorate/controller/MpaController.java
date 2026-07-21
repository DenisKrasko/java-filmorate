package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {

	private final FilmService filmService;

	@GetMapping
	public Collection<Mpa> findAll() {
		return filmService.findAllMpa();
	}

	@GetMapping("/{id}")
	public Mpa findById(@PathVariable Long id) {
		return filmService.findMpaById(id);
	}
}