package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.Setter;

public class ErrorResponse {
	@Getter@Setter
	private String error;

	public ErrorResponse(String error) {
		this.error = error;
	}

	public String getError() {
		return error;
	}
}