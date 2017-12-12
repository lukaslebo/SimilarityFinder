package ch.propulsion.similarityfinder.dbUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ch.propulsion.similarityfinder.service.entity.UserService;

@Component
public class DataBaseLoader implements ApplicationRunner {
	
	private final UserService userService;
	
	@Autowired
	public DataBaseLoader(UserService userService) {
		this.userService = userService;
	}
	
	@Scheduled(fixedRate=180*1000)
	public void deleteExpiredUsers() {
		this.userService.deleteExpiredUsers();
	}
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		
	}

}
