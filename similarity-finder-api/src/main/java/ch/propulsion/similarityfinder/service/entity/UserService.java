package ch.propulsion.similarityfinder.service.entity;

import java.time.LocalDateTime;
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
	void setExpirationDate(String userId, LocalDateTime newDate);
	
	void setDocument(String userId, Document document);
	void removeDocument(String userId);
	void addResource(String userId, Document document);
	void removeResource(String userId, String resourceId);
	void parseAll(String userId);
	void addSimilarity(String userId, Similarity similarity);
	void removeAllSimilarities(String userId);
	List<String> getResourceIds(String userId);
	List<Document> getResources(String userId);
	Document getDocument(String userId);
	List<Similarity> getSimilarities(String userId);
	
}
