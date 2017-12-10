package ch.propulsion.similarityfinder.service.detection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ch.propulsion.similarityfinder.domain.Similarity;

public class DetailComparatorRecursive implements DetailComparator {
	
	private static final int MIN_WORDS = 3;
	private static final double SIM_THRESHHOLD = 0.85;
	
	private final List<String> words1;
	private final List<String> words2;
	private final String resourceId;
	private final int docStart;
	private final int resStart;
	private int minSize1;
	private int minSize2;
	private int count = 0;
	
	private List<Similarity> detectedSimilarities = new ArrayList<>();
	private List<Similarity> uniqueSimilarities = new ArrayList<>();
	
	private final SorensenDice dice = new SorensenDice();
	
	public DetailComparatorRecursive(String documentString, String resourceString, String resourceId, int docStart, int resStart) {
		this.words1 = Arrays.asList(documentString.split(" "));
		this.words2 = Arrays.asList(resourceString.split(" "));
		this.resourceId = resourceId;
		this.docStart = docStart;
		this.resStart = resStart;
		System.err.println("in detail comparator:");
		System.err.println("words1: " + words1.size() + " | " + words1.toString());
		System.err.println("words2: " + words2.size() + " | " + words2.toString());
	}
	
	public List<Similarity> findSimilarities() {
		combinationLooper();
		System.err.println("count: " + count);
		filterSimilarities();
		return uniqueSimilarities;
	}
	
	private void combinationLooper() {
		minSize1 = Math.min(MIN_WORDS, words1.size());
		minSize2 = Math.min(MIN_WORDS, words2.size());
		combinationLooperRecursive(0, 0, words1.size(), words2.size(), 0);
	}
	
	private void combinationLooperRecursive(int start1, int start2, int end1, int end2, double prevSim) {
		if (end1 - start1 < minSize1 || end2 - start2 < minSize2) {
			return;
		}
		++count;
		String substring1 = String.join(" ", words1.subList(start1, end1));
		String substring2 = String.join(" ", words2.subList(start2, end2));
		double sim = dice.similarity(substring1, substring2);
//		System.err.println("substring1: " + substring1);
//		System.err.println("substring2: " + substring2);
//		System.err.println("=> " + sim);
		if (prevSim < sim || 0.5 < sim) {
			if (SIM_THRESHHOLD <= sim) {
				Similarity similarity = new Similarity(docStart+start1, docStart+end1-1, resourceId,//
																resStart+start2, resStart+end2-1, sim);
				detectedSimilarities.add(similarity);
			}
			combinationLooperRecursive(start1, start2, end1, end2-1, sim);
			combinationLooperRecursive(start1, start2, end1-1, end2, sim);
			combinationLooperRecursive(start1, start2+1, end1, end2, sim);
			combinationLooperRecursive(start1+1, start2, end1, end2, sim);
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
