package ch.propulsion.similarityfinder.service.detection;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.propulsion.similarityfinder.domain.Document;
import ch.propulsion.similarityfinder.domain.Similarity;
import ch.propulsion.similarityfinder.service.entity.UserService;
import ch.propulsion.similarityfinder.web.ApplicationController;

@Service
public class DocumentComparator {
	
	@Autowired
	private ApplicationController appController;
	
	private final UserService userService;
	private final SorensenDice dice;
	
	private String userId;
	private Document document;
	private List<Document> resources;
	private int TOTAL_CYCLES;
	private int completedCycles;
	private int progress;

	@Autowired
	public DocumentComparator(UserService userService, SorensenDice dice) {
		this.userService = userService;
		this.dice = dice;
	}
	
	public void initiateDetection(String userId) {
		this.userId = userId;
		document = userService.getDocument(userId);
		resources = userService.getResources(userId);
		if (document == null || resources == null || resources.size() == 0) {
			reset();
			return;
		}
		userService.parseAll(userId);
		prepareProgressTracking();
		documentLooper();
		reset();
		appController.updateProcessDone(userId);
	}

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
	}
	
	private void documentLooper() {
		for (Document resource : resources) {
			compareDocuments(document, resource);
		}
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
		int wordCount1;
		int wordCount2;
		double sim;
		
		for (int i = 0; i < startIndex.size(); i++) {
			
			docStart = startIndex.get(i);
			docEnd = endIndex.get(i);
			wordCount1 = docEnd - docStart + 1;
			
			for (int j = 0; j < resourceStartIndex.size(); j++) {
				
				resStart = resourceStartIndex.get(j);
				resEnd = resourceEndIndex.get(j);
				wordCount2 = resEnd - resStart + 1;
				String s1 = document.getSubset(docStart, docEnd);
				String s2 = resource.getSubset(resStart, resEnd);
				
				sim = dice.similarity(s1, s2);
				
				if (sentenceThreshhold(s1, wordCount1, s2, wordCount2) < sim) {
					
					detailComparator = new DetailComparator(s1, s2, resource.getId(), docStart, resStart);
					
					List<Similarity> similarityList = detailComparator.findSimilarities();
					for (Similarity similarity : similarityList) {
						userService.addSimilarity(userId, similarity);
					}
				}
				
				++completedCycles;
				trackProgress();
				
			}
		}	
	}
	
	private double sentenceThreshhold(String s1, int wordCount1, String s2, int wordCount2) {
		int wordCount = Math.max(wordCount1, wordCount2);
		int stringLength = Math.max(s1.length(), s2.length());
		return Math.min(wordBasedThreshhold(wordCount), lengthBasedThreshhold(stringLength));
	}
	
	private double wordBasedThreshhold(int wordCount) {
		int lowerLimit = 3;
		int upperLimit = 70;
		double minTH = 0;
		double maxTH = 0.6;
		return computeThreshhold(wordCount, lowerLimit, upperLimit, minTH, maxTH);
	}
	
	private double lengthBasedThreshhold(int stringLength) {
		int lowerLimit = 10;
		int upperLimit = 400;
		double minTH = 0;
		double maxTH = 0.6;
		return computeThreshhold(stringLength, lowerLimit, upperLimit, minTH, maxTH);
	}
	
	private double computeThreshhold(int n, int lowerLimit, int upperLimit, double minTH, double maxTH) {
		if (n <= lowerLimit) {
			return maxTH;
		}
		else if (n >= upperLimit) {
			return minTH;
		}
		else {
			return minTH + (maxTH - minTH) * (upperLimit - n) / (upperLimit - lowerLimit);
		}
	}
	
	private void trackProgress() {
		int currentProgress = Math.floorDiv(completedCycles * 100, TOTAL_CYCLES);
		if (currentProgress > progress) {
			progress = (int) currentProgress;
			appController.updateProgressInfo(userId, progress);
		}
	}
	
}
