package qa_live_test_app.controller;

import qa_live_test_app.model.ErrorResponse;
import qa_live_test_app.model.User;
import qa_live_test_app.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

	private final UserRepository userRepository;

	@PostMapping
	public ResponseEntity<Object> registerUser(
			@RequestBody
			User user
	) {
		if (!userRepository.getUserByPersonId(user.getPersonId()).isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse("Person with person Id already registered"));
		}
		return ResponseEntity.ok(userRepository.save(user));
	}

	@GetMapping
	public List<User> getUsers() {
		return userRepository.findAll();
	}

	@GetMapping("/{personId}")
	public ResponseEntity<Object> getUserByIdentificationNumber(
			@PathVariable
			Integer personId
	) {
		if (userRepository.getUserByPersonId(personId).isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse("Person with person Id not found"));
		}
		return ResponseEntity.ok(userRepository.getUserByPersonId(personId).getFirst());
	}
}
