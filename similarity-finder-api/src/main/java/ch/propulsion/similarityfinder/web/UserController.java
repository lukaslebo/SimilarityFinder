package ch.propulsion.similarityfinder.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.propulsion.similarityfinder.domain.User;
import ch.propulsion.similarityfinder.service.UserService;

@RestController
@RequestMapping(path="/api/user")
public class UserController {
	
	private final UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public Map<String, Object> createNewUser() {
		Map<String, Object> response = new HashMap<>();
		User user = this.userService.save(new User());
		response.put("id", user.getId());
		response.put("expiresAt", user.getExpirationDate());
		return response;
	}
	
	@GetMapping("/{userId}")
	public Map<String, Object> getUserById(@PathVariable String userId) {
		Map<String, Object> response = new HashMap<>();
		User user = this.userService.findById(userId);
		if (user == null) {
			response.put("user_exists", false);
		}
		else {
			response.put("user_exists", true);
			response.put("id", user.getId());
			response.put("expiresAt", user.getExpirationDate());
		}
		return response;
	}
	
	@PutMapping("/{userId}")
	public Map<String, Object> refreshExpirationPeriod(@PathVariable String userId) {
		Map<String, Object> response = new HashMap<>();
		User user = this.userService.findById(userId);
		if (user == null) {
			response.put("user_exists", false);
		}
		else {
			user.refreshExpirationDate();
			response.put("user_exists", true);
			response.put("id", user.getId());
			response.put("expiresAt", user.getExpirationDate());
		}
		return response;
	}
		
}
