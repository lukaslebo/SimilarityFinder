package ch.propulsion.similarityfinder.web;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ch.propulsion.similarityfinder.domain.Document;
import ch.propulsion.similarityfinder.domain.User;
import ch.propulsion.similarityfinder.service.entity.UserNotFoundException;
import ch.propulsion.similarityfinder.service.entity.UserService;
import ch.propulsion.similarityfinder.service.parser.DocumentParser;
import ch.propulsion.similarityfinder.service.storage.StorageService;

@RestController
@RequestMapping("/api/document")
public class DocumentController {
	
	private final UserService userService;
	private final StorageService storageService;
	private final DocumentParser parser;
	
	@Autowired
	public DocumentController(UserService userService, StorageService storageService, DocumentParser parser) {
		this.userService = userService;
		this.storageService = storageService;
		this.parser = parser;
	}
	
	@GetMapping("/{userId}")
	public Map<String, Object> getDocuments(@PathVariable String userId) {
		Map<String, Object> response = new HashMap<>();
		try {
			User user = this.userService.findById(userId);
			if (user == null) {
				throw new UserNotFoundException("User with id: " + userId + " does not exist.");
			}
			List<Document> resources = userService.getResources(userId);
			resources.sort((a, b) -> a.getDateCreated().isBefore(b.getDateCreated()) ? -1 : 1);
			response.put("user_exists", true);
			response.put("id", user.getId());
			response.put("expiresAt", user.getExpirationDate());
			response.put("document", userService.getDocument(userId));
			response.put("resources", resources);
		} catch (Exception e) {
			System.err.println("Error in retrieving documents of user!");
			System.err.println(e);
			e.printStackTrace(System.out);
			response.put("status", "failed");
			response.put("exception", e);
		}
		return response;
	}
	
	@PostMapping("/{userId}/setDoc")
	public Map<String, Object> setDocument(@PathVariable String userId, @RequestParam("file") MultipartFile file) {
		Map<String, Object> response = new HashMap<>();
		try {
			User user = this.userService.findById(userId);
			if (user == null) {
				throw new UserNotFoundException("User with id: " + userId + " does not exist.");
			}
			storageService.store(file);
			Path pathToFile = storageService.load(file.getOriginalFilename());
			Document doc = parser.parseFileToDocument(pathToFile);
			userService.setDocument(userId, doc);
			userService.parseAll(userId);
			response.put("status", "ok");
			response.put("document", userService.getDocument(userId));
		} catch (Exception e) {
			System.err.println("Error in file upload!");
			System.err.println(e);
			e.printStackTrace(System.out);
			response.put("status", "failed");
			response.put("exception", e);
		} finally {
			this.storageService.deleteAll();
		}
		return response;
	}
	
	@DeleteMapping("/{userId}/removeDocument")
	public Map<String, Object> removeDocument(@PathVariable String userId) {
		Map<String, Object> response = new HashMap<>();
		try {
			User user = this.userService.findById(userId);
			if (user == null) {
				throw new UserNotFoundException("User with id: " + userId + " does not exist.");
			}
			userService.removeDocument(userId);
			response.put("status", "ok");			
//			response.put("document", userService.getDocument(userId));
		} catch (Exception e) {
			System.err.println("Error in removing document.");
			System.err.println(e);
			e.printStackTrace(System.out);
			response.put("status", "failed");
			response.put("exception", e);
		}
		return response;
	}
	
	@PostMapping("/{userId}/addResource")
	public Map<String, Object> addResource(@PathVariable String userId, @RequestParam("file") MultipartFile[] files) {
		Map<String, Object> response = new HashMap<>();
		try {
			User user = this.userService.findById(userId);
			if (user == null) {
				throw new UserNotFoundException("User with id: " + userId + " does not exist.");
			}
			for (MultipartFile file : files) {
				storageService.store(file);				
			}
			Stream<Path> paths = storageService.loadAll();
			List<Document> docs = parser.parseFilesToDocument(paths);
			for (Document doc : docs) {
				userService.addResource(userId, doc);
			}
			userService.parseAll(userId);
			List<Document> resources = userService.getResources(userId);
			resources.sort((a, b) -> a.getDateCreated().isBefore(b.getDateCreated()) ? -1 : 1);
			response.put("status", "ok");
			response.put("resources", resources);
		} catch (Exception e) {
			System.err.println("Error in file upload!");
			System.err.println(e);
			e.printStackTrace(System.out);
			response.put("status", "failed");
			response.put("exception", e);
		} finally {
			this.storageService.deleteAll();
		}
		return response;
	}
	
	@DeleteMapping("/{userId}/removeResource/{resourceId}")
	public Map<String, Object> removeResource(@PathVariable String userId, @PathVariable String resourceId) {
		Map<String, Object> response = new HashMap<>();
		try {
			User user = this.userService.findById(userId);
			if (user == null) {
				throw new UserNotFoundException("User with id: " + userId + " does not exist.");
			}
			userService.removeResource(userId, resourceId);
			response.put("status", "ok");
//			response.put("resources", userService.getResources(userId));			
		} catch (Exception e) {
			System.err.println("Error in removing a resource.");
			System.err.println(e);
			e.printStackTrace(System.out);
			response.put("status", "failed");
			response.put("exception", e);
		}
		return response;
	}
	
	@PostMapping("/{userId}/setDocText")
	public Map<String, Object> setDocText(@PathVariable String userId, @RequestBody Map<String, String> body) {
		Map<String, Object> response = new HashMap<>();
		try {
			User user = this.userService.findById(userId);
			if (user == null) {
				throw new UserNotFoundException("User with id: " + userId + " does not exist.");
			}
			String content = body.get("text");
			String fileName = body.get("title");
			if (content == null || fileName == null) {
				throw new RequestRejectedException("Request body has to contain a property text and title.");
			}
			Document doc = new Document(content, fileName);
			userService.setDocument(userId, doc);
			userService.parseAll(userId);
			response.put("status", "ok");
			response.put("document", userService.getDocument(userId));			
		} catch ( Exception e) {
			System.err.println("Error in setting text to document.");
			System.err.println(e);
			e.printStackTrace(System.out);
			response.put("status", "failed");
			response.put("exception", e);
		}
		return response;
	}
	
	@PostMapping("/{userId}/addResourceText")
	public Map<String, Object> addResourceText(@PathVariable String userId, @RequestBody Map<String, String> body) {
		Map<String, Object> response = new HashMap<>();
		try {
			User user = this.userService.findById(userId);
			if (user == null) {
				throw new UserNotFoundException("User with id: " + userId + " does not exist.");
			}
			String content = body.get("text");
			String fileName = body.get("title");
			if (content == null || fileName == null) {
				throw new RequestRejectedException("Request body has to contain a property text and title.");
			}
			Document doc = new Document(content, fileName);
			userService.addResource(userId, doc);
			userService.parseAll(userId);
			List<Document> resources = userService.getResources(userId);
			resources.sort((a, b) -> a.getDateCreated().isBefore(b.getDateCreated()) ? -1 : 1);
			response.put("status", "ok");
			response.put("resources", resources);		
		} catch ( Exception e) {
			System.err.println("Error in adding text to resources.");
			System.err.println(e);
			e.printStackTrace(System.out);
			response.put("status", "failed");
			response.put("exception", e);
		}
		return response;
	}
	
}
