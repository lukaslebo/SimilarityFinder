package ch.propulsion.similarityfinder.service.detection;

import java.util.List;

import ch.propulsion.similarityfinder.domain.Similarity;

public interface DetailComparator {
	List<Similarity> findSimilarities();
}
