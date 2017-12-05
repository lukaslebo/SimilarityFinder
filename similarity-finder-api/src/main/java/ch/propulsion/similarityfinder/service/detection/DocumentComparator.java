package ch.propulsion.similarityfinder.service.detection;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.propulsion.similarityfinder.domain.Document;
import ch.propulsion.similarityfinder.service.entity.UserService;

@Service
public class DocumentComparator {
	
	private final UserService userService;
	private final SorensenDice dice;
	
	private Document document;
	private List<Document> resources;

	@Autowired
	public DocumentComparator(UserService userService, SorensenDice dice) {
		this.userService = userService;
		this.dice = dice;
	}
	
	public void initiateDetection(String userId) {
		document = userService.getDocument(userId);
		resources = userService.getResources(userId);
		if (document == null || resources == null || resources.size() == 0) {
			reset();
			return;
		}
		userService.parseAll(userId);
		documentLooper();
	}
	
	private void documentLooper() {
		for (Document resource : resources) {
			// compare document vs resource
		}
	}
	
	private void reset() {
		document = null;
		resources = null;
	}
	
	
	
	
	
}
