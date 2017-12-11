package ch.propulsion.similarityfinder.service.detection;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ch.propulsion.similarityfinder.domain.Document;
import ch.propulsion.similarityfinder.domain.Similarity;
import ch.propulsion.similarityfinder.service.entity.SimilarityService;
import ch.propulsion.similarityfinder.service.entity.UserService;
import ch.propulsion.similarityfinder.web.ApplicationController;

public class DocumentComparator {
	
	/*
	 * DEPENDENCIES
	 */
	@Autowired
	private ApplicationController appController;
	@Autowired
	private UserService userService;
	@Autowired
	private SimilarityService simService;
	@Autowired
	private SorensenDice dice;

	/*
	 * FIELDS
	 */
	private String userId;
	private Document document;
	private List<Document> resources;
	private int TOTAL_CYCLES;
	private int completedCycles;
	private int progress;
	private int countDetail = 0;

	/*
	 * INITIATE DETECTION
	 */
	public void initiateDetection(String userId) {
		this.userId = userId;
		document = userService.getDocument(userId);
		resources = userService.getResources(userId);
		if (document == null || resources == null || resources.size() == 0) {
			reset();
			appController.updateProcessDone(userId);
			return;
		}
		userService.removeAllSimilarities(userId);
		prepareProgressTracking();
		documentLooper();
		reset();
		appController.updateProcessDone(userId);
	}
	
	/*
	 * PRIVATE METHODS
	 */
	private void reset() {
		document = null;
		resources = null;
		completedCycles = 0;
		TOTAL_CYCLES = 0;
	}
	
	private void prepareProgressTracking() {
		
		completedCycles = 0;
		progress = 0;
		
		int total = 0;
		int docSize = document.getSentenceStartIndex().size();
		
		for (Document resource : resources) {
			
			int resSize = resource.getSentenceStartIndex().size();
			total += docSize * resSize;
		}
		TOTAL_CYCLES = total;
		
		appController.updateProgressInfo(userId, progress);
	}
	
	private void documentLooper() {
		Long time = System.nanoTime();
		for (Document resource : resources) {
			compareDocuments(document, resource);
		}
		time = Math.round((System.nanoTime()-time)/Math.pow(10,9)); 
		System.err.println("time required is " + time + " seconds");
		System.err.println("DetailComparator called " + countDetail + " times");
	}
	
	private void compareDocuments(Document document, Document resource) {		
		final List<Integer> startIndex = document.getSentenceStartIndex();
		final List<Integer> endIndex = document.getSentenceEndIndex();
		final List<Integer> resourceStartIndex = resource.getSentenceStartIndex();
		final List<Integer> resourceEndIndex = resource.getSentenceEndIndex();
		DetailComparator detailComparator;
		int docStart;
		int docEnd;
		int resStart;
		int resEnd;
		int size1;
		int size2;
		double sim;
		
		for (int i = 0; i < startIndex.size(); i++) {
			
			docStart = startIndex.get(i);
			docEnd = endIndex.get(i);
			size1 = docEnd - docStart + 1;
			
			for (int j = 0; j < resourceStartIndex.size(); j++) {
				
				resStart = resourceStartIndex.get(j);
				resEnd = resourceEndIndex.get(j);
				size2 = resEnd - resStart + 1;
				String s1 = document.getSubset(docStart, docEnd);
				String s2 = resource.getSubset(resStart, resEnd);
				
				sim = dice.similarity(s1, s2);
				
				if (dynamicThreshold(size1, size2, s1.length(), s2.length()) < sim) {
					++countDetail;
					detailComparator = new DetailComparatorIterative(s1, s2, resource.getId(), docStart, resStart);
					
					List<Similarity> similarityList = detailComparator.findSimilarities();
					for (Similarity similarity : similarityList) {
						similarity = simService.save(similarity);
						userService.addSimilarity(userId, similarity);
					}
				}
				
				++completedCycles;
				trackProgress();
				
			}
		}
		
	}
	
	private double dynamicThreshold(int size1, int size2, int length1, int length2) {
		double size = Math.max(size1, size2);
		double wordThreshold = Math.max(Math.pow(3/(size+3), size/70)*0.65,  -0.01*size+0.63);
		
		double length = Math.max(length1, length2);
		double lengthThreshold = Math.max(0.7*Math.pow(1/(length+1), length/800), -0.00154*length+0.6);
		
		return Math.min(wordThreshold, lengthThreshold);
	}
	
	private void trackProgress() {
		int currentProgress = Math.floorDiv(completedCycles * 100, TOTAL_CYCLES);
		if (currentProgress > progress) {
			progress = (int) currentProgress;
			appController.updateProgressInfo(userId, progress);
		}
	}
	
}
