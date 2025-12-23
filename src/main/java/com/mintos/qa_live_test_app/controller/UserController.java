package com.mintos.qa_live_test_app.controller;

import com.mintos.qa_live_test_app.model.User;
import com.mintos.qa_live_test_app.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

	private final UserRepository userRepository;

	@PostMapping
	public User registerUser(
			@RequestBody
			User user
	) {
		if (!userRepository.getUserByPersonId(user.getPersonId()).isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Person with person Id already registered");
		}
		return userRepository.save(user);
	}

	@GetMapping
	public List<User> getUsers() {
		return userRepository.findAll();
	}

	@GetMapping("/{personId}")
	public User getUserByIdentificationNumber(
			@PathVariable
			Integer personId
	) {
		if (userRepository.getUserByPersonId(personId).isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person with person Id not found");
		}
		return userRepository.getUserByPersonId(personId).getFirst();
	}
}
