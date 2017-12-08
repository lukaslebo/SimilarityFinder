package ch.propulsion.similarityfinder.dbUtils;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ch.propulsion.similarityfinder.service.entity.DocumentService;
import ch.propulsion.similarityfinder.service.entity.SimilarityService;
import ch.propulsion.similarityfinder.service.entity.UserService;

@Component
public class DataBaseLoader implements ApplicationRunner {
	
	private final UserService userService;
//	private final SimilarityService similarityService;
//	private final DocumentService documentSercice;
	
	@Autowired
	public DataBaseLoader(UserService userService, SimilarityService similarityService, DocumentService documentSercice) {
		this.userService = userService;
//		this.similarityService = similarityService;
//		this.documentSercice = documentSercice;
	}
	
	@Scheduled(fixedRate=180*1000)
	public void deleteExpiredUsers() {
		System.out.println("Deleting expired users.");
		this.userService.deleteExpiredUsers();
	}
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		String userId = "4be84db3-0788-4140-9e97-f4f77d3e3b56";
		LocalDateTime expirationDate = LocalDateTime.now().plusYears(1);
		userService.setExpirationDate(userId, expirationDate);
	}

}
