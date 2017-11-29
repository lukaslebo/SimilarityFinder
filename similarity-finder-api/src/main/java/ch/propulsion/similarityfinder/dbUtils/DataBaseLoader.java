package ch.propulsion.similarityfinder.dbUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ch.propulsion.similarityfinder.domain.Document;
import ch.propulsion.similarityfinder.domain.Similarity;
import ch.propulsion.similarityfinder.domain.User;
import ch.propulsion.similarityfinder.service.DocumentService;
import ch.propulsion.similarityfinder.service.SimilarityService;
import ch.propulsion.similarityfinder.service.UserService;

@Component
public class DataBaseLoader implements ApplicationRunner {
	
	private final UserService userService;
	private final SimilarityService similarityService;
	private final DocumentService documentSercice;
	
	@Autowired
	public DataBaseLoader(UserService userService, SimilarityService similarityService, DocumentService documentSercice) {
		this.userService = userService;
		this.similarityService = similarityService;
		this.documentSercice = documentSercice;
	}
	
	@Scheduled(fixedDelay=180*1000)
	public void deleteExpiredUsers() {
		System.out.println("Deleting expired users.");
		this.userService.deleteExpiredUsers();
	}
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
			
//		User user = this.userService.save(new User());
//		String userId = user.getId();
//		
//		Document thesis = new Document("This is the example test of my thesis. Bla Bla.");
//		thesis = this.documentSercice.save(thesis);
//		
//		Document resourceA = new Document("This is resource A that I used for my thesis. So nice!");
//		Document resourceB = new Document("And this is resource B that I used for my thesis. Even nicer!!");
//		resourceA = this.documentSercice.save(resourceA);
//		resourceB = this.documentSercice.save(resourceB);
//		
//		this.userService.setDocument(userId, thesis);
//		this.userService.addResource(userId, resourceA);
//		this.userService.addResource(userId, resourceB);
//		this.userService.parseAll(userId);
//		
//		Similarity similarityA = new Similarity(0, 1, resourceA.getId(), 0, 1, 2);
//		Similarity similarityB = new Similarity(0, 1, resourceB.getId(), 1, 2, 3);
//		similarityA = this.similarityService.save(similarityA);
//		similarityB = this.similarityService.save(similarityB);
//		
//		this.userService.addSimilarity(userId, similarityA);
//		this.userService.addSimilarity(userId, similarityB);
		
//		this.userService.removeDocument(userId);
//		this.userService.removeResource(userId, resourceB.getId());
		
//		this.userService.deleteById(user.getId());
		
	}

}
