package ch.propulsion.similarityfinder.service.detection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ch.propulsion.similarityfinder.domain.Similarity;

public class DetailComparator {
	
	private static final int MIN_WORDS = 3;
	private static final double SIM_THRESHHOLD = 0.85;
	
	private final List<String> words1;
	private final List<String> words2;
	private final String resourceId;
	private final int docStart;
	private final int resStart;
	
	private List<Similarity> detectedSimilarities = new ArrayList<>();
	private List<Similarity> uniqueSimilarities = new ArrayList<>();
	
	private final SorensenDice dice = new SorensenDice();
	
	public DetailComparator(String documentString, String resourceString, String resourceId, int docStart, int resStart) {
		this.words1 = Arrays.asList(documentString.split(" "));
		this.words2 = Arrays.asList(resourceString.split(" "));
		this.resourceId = resourceId;
		this.docStart = docStart;
		this.resStart = resStart;
	}
	
	public List<Similarity> findSimilarities() {
		combinationLooper();
		filterSimilarities();
		return uniqueSimilarities;
	}
	
	private void combinationLooper() {
		int minSize1 = Math.min(MIN_WORDS, words1.size());
		int minSize2 = Math.min(MIN_WORDS, words2.size());
		
		for (int size1 = words1.size(); size1 >= minSize1; size1--) {
			for (int start1 = 0; start1+size1 <= words1.size(); start1++) {
				
				String substring1 = String.join(" ", words1.subList(start1, start1+size1));
				
				for (int size2 = words2.size(); size2 >= minSize2; size2--) {
					for (int start2 = 0; start2+size2 >= words2.size(); start2++) {
						
						String substring2 = String.join(" ", words2.subList(start2, start2+size2));
						
						double sim = dice.similarity(substring1, substring2);
						
						if (SIM_THRESHHOLD <= sim) {
							Similarity similarity = new Similarity(docStart+start1, docStart+start1+size1-1, resourceId,//
																				resStart+start2, resStart+start2+size2-1, sim);
							detectedSimilarities.add(similarity);
						}
						
					}
				}
			}
		}
	}
	
	private void filterSimilarities() {
		for (Similarity similarity : detectedSimilarities) {
			compareToAll(similarity);
		}
		setUniqueSimilarities();
		reduceUniqueSimilarities();
	}
	
	private void compareToAll(Similarity similarity) {
		if (!similarity.isUnique()) {
			return;
		}
		for (Similarity comparingSim : detectedSimilarities) {
			if (comparingSim.isUnique()) {
				compareSimilarities(similarity, comparingSim);
			}
		}
	}
	
	private void compareSimilarities(Similarity sim1, Similarity sim2) {
		// Don't compare object with itself
		if (sim1 == sim2) {
			return;
		}
		if (sim1.isContainedIn(sim2) || sim2.isContainedIn(sim1) || sim1.isOverlappingWith(sim2)) {
			deselectLesserSimilarity(sim1, sim2);
		}
	}
	
	private void deselectLesserSimilarity(Similarity sim1, Similarity sim2) {
		if (sim1.getSimilarity() == sim2.getSimilarity()) {
			if (sim1.size() >= sim2.size()) {
				sim2.setUnique(false);
			} else {
				sim1.setUnique(false);
			}
		} else if (sim1.getSimilarity() > sim2.getSimilarity()) {
			sim2.setUnique(false);
		} else {
			sim1.setUnique(false);
		}
	}
	
	private void reduceUniqueSimilarities() {
		List<Similarity> reducedSimilarities = new ArrayList<>();
		if (uniqueSimilarities.size() < 2) {
			return;
		}
		List<List<Similarity>> pairs = getAllPairs();
		for (List<Similarity> pair : pairs) {
			Similarity sim1 = pair.get(0);
			Similarity sim2 = pair.get(1);
			int startIndex = Math.min(sim1.getStartIndex(), sim2.getStartIndex())-docStart;
			int endIndex = Math.max(sim1.getEndIndex(), sim2.getEndIndex())-docStart;
			int resourceStartIndex = Math.min(sim1.getResourceStartIndex(), sim2.getResourceStartIndex())-resStart;
			int resourceEndIndex = Math.max(sim1.getResourceEndIndex(), sim2.getResourceEndIndex())-resStart;
			String s1 = String.join(" ", words1.subList(startIndex, endIndex+1));
			String s2 = String.join(" ", words2.subList(resourceStartIndex, resourceEndIndex+1));
			double sim = dice.similarity(s1, s2);
			if (SIM_THRESHHOLD <= sim) {
				Similarity reducedSim = new Similarity(startIndex + docStart, endIndex+docStart, resourceId,//
						resourceStartIndex+resStart, resourceEndIndex+resStart, sim);
				uniqueSimilarities = uniqueSimilarities.stream().filter(el -> !el.isContainedIn(reducedSim)).collect(Collectors.toList());
				reducedSimilarities.add(reducedSim);
			}
		}
		for (int i = 0; i < reducedSimilarities.size(); i++) {
			Similarity sim1 = reducedSimilarities.get(i);
			for (int j = i+1; j < reducedSimilarities.size(); j++) {
				Similarity sim2 = reducedSimilarities.get(j);
				if (sim1.isContainedIn(sim2)) {
					sim1.setUnique(false);
				} else if (sim2.isContainedIn(sim1)) {
					sim2.setUnique(false);
				}
			}
		}
		reducedSimilarities = reducedSimilarities.stream().filter(el -> el.isUnique()).collect(Collectors.toList());
		uniqueSimilarities.addAll(reducedSimilarities);
	}
	
	private List<List<Similarity>> getAllPairs() {
		List<List<Similarity>> pairs = new ArrayList<>();
		for (int i = 0; i < uniqueSimilarities.size(); i++) {
			Similarity sim1 = uniqueSimilarities.get(i);
			for (int j = i+1; j < uniqueSimilarities.size(); j++) {
				Similarity sim2 = uniqueSimilarities.get(j);
				pairs.add(Arrays.asList(sim1, sim2));
			}
		}
		return pairs;
	}
	
	private void setUniqueSimilarities() {
		uniqueSimilarities = detectedSimilarities.stream()//
											.filter(sim -> sim.isUnique())//
											.collect(Collectors.toList());
	}
	
}
