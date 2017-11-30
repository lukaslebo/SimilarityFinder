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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ch.propulsion.similarityfinder.domain.Document;
import ch.propulsion.similarityfinder.domain.User;
import ch.propulsion.similarityfinder.service.DocumentService;
import ch.propulsion.similarityfinder.service.UserService;
import ch.propulsion.similarityfinder.service.storage.StorageService;

@RestController
@RequestMapping("/api/document")
public class DocumentController {
	
	private final DocumentService documentService;
	private final UserService userService;
	private final StorageService storageService;
	
	private List<String> files = new ArrayList<>();
	
	@Autowired
	public DocumentController(DocumentService documentService, UserService userService, StorageService storageService) {
		this.documentService = documentService;
		this.userService = userService;
		this.storageService = storageService;
	}
	
	@GetMapping("/{userId}")
	public Map<String, Object> getDocuments(@PathVariable String userId) {
		Map<String, Object> response = new HashMap<>();
		User user = this.userService.findById(userId);
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
	
	@PostMapping("/{userId}/setDoc")
	public Map<String, Object> setDocument(@PathVariable String userId, @RequestParam("file") MultipartFile file) {
		try {
			storageService.store(file);
			System.err.println("file stored successfully");
			files.add(file.getOriginalFilename());
		} catch (Exception e) {
			System.err.println("Error in file upload!");
		}
		Map<String, Object> response = new HashMap<>();
		response.put("text","test");
		return response;
	}
	
	@PostMapping("/{userId}/removeDoc")
	public Map<String, Object> removeDocument(@PathVariable String userId) {
		return null;
	}
	
	@PostMapping("/{userId}/addResource")
	public Map<String, Object> addResource(@PathVariable String userId) {
		return null;
	}
	
	@PostMapping("/{userId}/removeResource")
	public Map<String, Object> removeResource(@PathVariable String userId) {
		return null;
	}
	
}
