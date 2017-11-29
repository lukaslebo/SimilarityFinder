package ch.propulsion.similarityfinder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.propulsion.similarityfinder.domain.Similarity;

@Repository
public interface SimilarityRepository extends JpaRepository<Similarity, String> {
	
	Similarity findById(String id);
	List<Similarity> findByResourceId(String resourceId);
	List<Similarity> findAll();
	void deleteById(String id);
	void deleteAllByResourceId(String resourceId);
	
}
