package ch.propulsion.similarityfinder.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.propulsion.similarityfinder.domain.Similarity;
import ch.propulsion.similarityfinder.repository.SimilarityRepository;

@Service
@Transactional
public class DefaultSimilarityService implements SimilarityService {
	
	private final SimilarityRepository similarityRepository;
	
	@Autowired
	public DefaultSimilarityService(SimilarityRepository similarityRepository) {
		this.similarityRepository = similarityRepository;
	}
	
	@Override
	public Similarity save(Similarity similarity) {
		return this.similarityRepository.save(similarity);
	}

	@Override
	public Similarity findById(String id) {
		return this.similarityRepository.findById(id);
	}

	@Override
	public List<Similarity> findAll() {
		return this.similarityRepository.findAll();
	}

	@Override
	public void deleteById(String id) {
		this.similarityRepository.deleteById(id);
	}

}
