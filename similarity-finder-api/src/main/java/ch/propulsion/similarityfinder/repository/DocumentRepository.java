package ch.propulsion.similarityfinder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.propulsion.similarityfinder.domain.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, String>{

	Document findById(String id);
	List<Document> findAll();
	void deleteById(String id);
	
}
