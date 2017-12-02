package ch.propulsion.similarityfinder.service.entity;

import java.util.List;

import org.springframework.stereotype.Service;

import ch.propulsion.similarityfinder.domain.Document;
import ch.propulsion.similarityfinder.domain.Similarity;
import ch.propulsion.similarityfinder.domain.User;

@Service
public interface UserService {
	
	User save(User user);
	List<User> findAll();
	User findById(String id);
	void deleteById(String id);
	void deleteExpiredUsers();
	
	void setDocument(String userId, Document document);
	void removeDocument(String userId);
	void addResource(String userId, Document document);
	void removeResource(String userId, String resourceId);
	void parseAll(String userId);
	void addSimilarity(String userId, Similarity similarity);
	List<String> getResourceIds(String userId);
	List<Document> getResources(String userId);
	Document getDocument(String userId);
	
}
