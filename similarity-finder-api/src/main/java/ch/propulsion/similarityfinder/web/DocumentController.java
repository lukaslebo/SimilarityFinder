package ch.propulsion.similarityfinder.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.propulsion.similarityfinder.domain.Document;
import ch.propulsion.similarityfinder.domain.User;
import ch.propulsion.similarityfinder.service.DocumentService;
import ch.propulsion.similarityfinder.service.UserService;

@RestController
@RequestMapping("/api/document")
public class DocumentController {
	
	private final DocumentService documentService;
	private final UserService userService;
	
	@Autowired
	public DocumentController(DocumentService documentService, UserService userService) {
		this.documentService = documentService;
		this.userService = userService;
	}
	
	@GetMapping("/{id}")
	public Map<String, Object> getDocuments(@PathVariable String id) {
		Map<String, Object> response = new HashMap<>();
		User user = this.userService.findById(id);
		if (user == null) {
			response.put("user_exists", false);
		}
		else {
			List<Document> resources = new ArrayList<Document>(user.getResources().values());
			resources.sort((a, b) -> a.getDateCreated().isBefore(b.getDateCreated()) ? -1 : 1);
			response.put("user_exists", true);
			response.put("id", user.getId());
			response.put("expiresAt", user.getExpirationDate());
			response.put("document", user.getDocument());
			response.put("resources", resources);
		}
		return response;
	}
	
	@PostMapping("/{id}/setDoc")
	public Map<String, Object> setDocument(@PathVariable String id) {
		return null;
	}
	
	@PostMapping("/{id}/removeDoc")
	public Map<String, Object> removeDocument(@PathVariable String id) {
		return null;
	}
	
	@PostMapping("/{id}/addResource")
	public Map<String, Object> addResource(@PathVariable String id) {
		return null;
	}
	
	@PostMapping("/{id}/removeResource")
	public Map<String, Object> removeResource(@PathVariable String id) {
		return null;
	}
	
}
