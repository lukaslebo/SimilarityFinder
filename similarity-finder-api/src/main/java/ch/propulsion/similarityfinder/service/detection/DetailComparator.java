package ch.propulsion.similarityfinder.service.detection;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import ch.propulsion.similarityfinder.domain.Document;
import ch.propulsion.similarityfinder.domain.Similarity;

@Component
public class DetailComparator {
	
	private final String s1;
	private final String s2;
	private final Document document;
	private final Document resource;
	
	private List<Similarity> detectedSimilarities = new ArrayList<>();
	
	public DetailComparator(String s1, String s2, Document document, Document resource) {
		this.s1 = s1;
		this.s2 = s2;
		this.document = document;
		this.resource = resource;
	}
	
	public List<Similarity> findSimilarities() {
		// find all real similarities
		// ...
		return detectedSimilarities;
	}
	
}
