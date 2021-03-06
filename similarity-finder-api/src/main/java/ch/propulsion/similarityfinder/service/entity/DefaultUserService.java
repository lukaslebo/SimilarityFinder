package ch.propulsion.similarityfinder.service.entity;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.propulsion.similarityfinder.domain.Document;
import ch.propulsion.similarityfinder.domain.Similarity;
import ch.propulsion.similarityfinder.domain.User;
import ch.propulsion.similarityfinder.repository.DocumentRepository;
import ch.propulsion.similarityfinder.repository.SimilarityRepository;
import ch.propulsion.similarityfinder.repository.UserRepository;

@Service
@Transactional
public class DefaultUserService implements UserService {
	
	private final UserRepository userRepository;
	private final SimilarityRepository similarityRepository;
	private final DocumentRepository documentRepository;
	
	@Autowired
	public DefaultUserService(UserRepository userRepository, SimilarityRepository similarityRepository, DocumentRepository documentRepository) {
		this.userRepository = userRepository;
		this.similarityRepository = similarityRepository;
		this.documentRepository = documentRepository;
	}
	
	@Override
	public User save(User user) {
		return this.userRepository.save(user);
	}

	@Override
	public List<User> findAll() {
		return this.userRepository.findAll();
	}

	@Override
	public User findById(String id) {
		return this.userRepository.findById(id);
	}

	@Override
	public void deleteById(String id) {
		User user = this.userRepository.findById(id);
		if (user == null) {
			return;
		}
		String userId = user.getId();
		this.removeDocument(userId);
		List<String> resourceIds = user.getResourceIds().stream().collect(Collectors.toList());
		for (String resourceId : resourceIds) {
			this.removeResource(userId, resourceId);
		}
		this.userRepository.deleteById(id);
	}

	@Override
	public void deleteExpiredUsers() {
		List<User> usersToDelete = this.userRepository.findByExpirationDateBefore(LocalDateTime.now(Clock.systemUTC()));
		for (User user : usersToDelete) {
			this.deleteById(user.getId());
		}
	}
	
	@Override
	public void setExpirationDate(String userId, LocalDateTime newDate) {
		User user = this.userRepository.findById(userId);
		if (user == null) {
			return;
		}
		user.setExpirationDate(newDate);
	}

	@Override
	public void setDocument(String userId, Document document) {
		User user = this.userRepository.findById(userId);
		if (user == null) {
			return;
		}
		if (user.getDocument() != null) {
			removeDocument(userId);
		}
		document = this.documentRepository.save(document);
		user.setDocument(document);
	}

	@Override
	public void removeDocument(String userId) {
		User user = this.userRepository.findById(userId);
		if (user == null || user.getDocument() == null) {
			return;
		}
		String documentId = user.getDocument().getId();
		List<String> resourceIds = user.getResourceIds().stream().collect(Collectors.toList());
		user.removeDocument();
		for (String resourceId  : resourceIds) {
			this.similarityRepository.deleteAllByResourceId(resourceId);
		}
		this.documentRepository.deleteById(documentId);
	}

	@Override
	public void addResource(String userId, Document document) {
		User user = this.userRepository.findById(userId);
		if (user == null) {
			return;
		}
		document = this.documentRepository.save(document);
		user.addResource(document.getId(), document);
	}

	@Override
	public void removeResource(String userId, String resourceId) {
		User user = this.userRepository.findById(userId);
		if (user == null) {
			return;
		}
		user.removeResource(resourceId);
		this.similarityRepository.deleteAllByResourceId(resourceId);
		this.documentRepository.deleteById(resourceId);
		
	}

	@Override
	public void addSimilarity(String userId, Similarity similarity) {
		User user = this.userRepository.findById(userId);
		if (user == null) {
			return;
		}
		user.addSimilarity(similarity);
	}
	
	@Override
	public void removeAllSimilarities(String userId) {
		User user = this.userRepository.findById(userId);
		if (user == null) {
			return;
		}
		List<Similarity> similarities = user.getSimilarities();
		user.setSimilarities(new ArrayList<>());
		for (Similarity similarity : similarities) {
			this.similarityRepository.deleteById(similarity.getId());
		}
	}

	@Override
	public List<String> getResourceIds(String userId) {
		User user = this.userRepository.findById(userId);
		if (user == null) {
			return null;
		}
		return user.getResourceIds().stream().collect(Collectors.toList());
	}

	@Override
	public List<Document> getResources(String userId) {
		User user = this.userRepository.findById(userId);
		if (user == null) {
			return null;
		}
		List<Document> resources = new ArrayList<>(user.getResources().values());
		for (Document resource : resources) {
			resource.parse();
		}
		return resources;
	}

	@Override
	public Document getDocument(String userId) {
		User user = this.userRepository.findById(userId);
		if (user == null) {
			return null;
		}
		Document document = user.getDocument();
		if (document == null) {
			return null;
		}
		document.parse();
		return document;
	}

	@Override
	public List<Similarity> getSimilarities(String userId) {
		User user = this.userRepository.findById(userId);
		if (user == null) {
			return null;
		}
		return user.getSimilarities();
	}

}
