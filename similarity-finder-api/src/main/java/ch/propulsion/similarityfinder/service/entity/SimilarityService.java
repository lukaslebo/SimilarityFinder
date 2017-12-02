package ch.propulsion.similarityfinder.service.entity;

import java.util.List;

import org.springframework.stereotype.Service;

import ch.propulsion.similarityfinder.domain.Similarity;

@Service
public interface SimilarityService {
	
	Similarity save(Similarity similarity);
	Similarity findById(String id);
	List<Similarity> findAll();
	void deleteById(String id);
	
	
}
