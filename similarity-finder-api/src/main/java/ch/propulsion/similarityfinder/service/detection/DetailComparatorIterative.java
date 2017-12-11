package ch.propulsion.similarityfinder.service.detection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ch.propulsion.similarityfinder.domain.Similarity;

public class DetailComparatorIterative implements DetailComparator {
	
	private static final int MIN_WORDS = 3;
	private static final double SIM_THRESHHOLD = 0.80;
	
	private final List<String> words1;
	private final List<String> words2;
	private final String resourceId;
	private final int docStart;
	private final int resStart;
	private int count = 0;
	
	private List<Similarity> detectedSimilarities = new ArrayList<>();
	private List<Similarity> uniqueSimilarities = new ArrayList<>();
	
	private final SorensenDice dice = new SorensenDice();
	
	public DetailComparatorIterative(String documentString, String resourceString, String resourceId, int docStart, int resStart) {
		this.words1 = Arrays.asList(documentString.split(" "));
		this.words2 = Arrays.asList(resourceString.split(" "));
		this.resourceId = resourceId;
		this.docStart = docStart;
		this.resStart = resStart;
//		System.err.println("in detail comparator:");
//		System.err.println("words1: " + words1.size() + " | " + words1.toString());
//		System.err.println("words2: " + words2.size() + " | " + words2.toString());
	}
	
	public List<Similarity> findSimilarities() {
		combinationLooper();
		filterSimilarities();
		System.err.println("count: " + count);
		return uniqueSimilarities;
	}
	
	private void combinationLooper() {
		int minSize1 = Math.min(MIN_WORDS, words1.size());
		int minSize2 = Math.min(MIN_WORDS, words2.size());
		
		int n1 = (words1.size()-1)*(words1.size()-minSize1+1)/2;
		int n2 = (words2.size()-1)*(words2.size()-minSize2+1)/2;
		List<String> substrings1 = new ArrayList<>(n1);
		List<String> substrings2 = new ArrayList<>(n2);
		List<Integer> start1 = new ArrayList<>(n1);
		List<Integer> end1 = new ArrayList<>(n1);
		List<Integer> start2 = new ArrayList<>(n2);
		List<Integer> end2 = new ArrayList<>(n2);
		
		for (int size1 = words1.size(); size1 >= minSize1; size1--) {
			for (int _start1 = 0; _start1+size1 <= words1.size(); _start1++) {
				substrings1.add(String.join(" ", words1.subList(_start1, _start1+size1)));
				start1.add(_start1);
				end1.add(_start1+size1-1);
			}
		}	
		for (int size2 = words2.size(); size2 >= minSize2; size2--) {
			for (int _start2 = 0; _start2+size2 <= words2.size(); _start2++) {
				substrings2.add(String.join(" ", words2.subList(_start2, _start2+size2)));
				start2.add(_start2);
				end2.add(_start2+size2-1);
			}
		}
		
		int i=0, j=0;
		for (String substring1 : substrings1) {
			for (String substring2 : substrings2) {
				++count;
				double sim = dice.similarity(substring1, substring2);
				if (SIM_THRESHHOLD <= sim) {
					Similarity similarity = new Similarity(docStart+start1.get(i), docStart+end1.get(i), resourceId,//
																	resStart+start2.get(j), resStart+end2.get(j), sim);
					detectedSimilarities.add(similarity);
				}
				++j;
			}
			j=0;
			++i;
		}
		
	}
	
	private void filterSimilarities() {
		for (Similarity similarity : detectedSimilarities) {
			compareToAll(similarity);
		}
		setUniqueSimilarities();
		System.err.println("unique similarities: " + uniqueSimilarities.size());
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
		if (Math.abs(sim1.getSimilarity() - sim2.getSimilarity()) <= Math.pow(10, -10)) {
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
