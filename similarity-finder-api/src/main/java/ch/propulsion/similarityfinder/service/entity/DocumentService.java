package ch.propulsion.similarityfinder.service.entity;

import java.util.List;

import ch.propulsion.similarityfinder.domain.Document;

public interface DocumentService {
	
	Document save(Document document);
	Document findById(String id);
	List<Document> findAll();
	void deleteById(String id);
	
}
