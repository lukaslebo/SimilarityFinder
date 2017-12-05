package ch.propulsion.similarityfinder.web;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.bind.annotation.RestController;

import ch.propulsion.similarityfinder.domain.User;
import ch.propulsion.similarityfinder.service.entity.UserService;

@RestController
public class ApplicationController {
	
	private Double dummyProgress = null;
	private ScheduledFuture<?> scheduledFuture = null;
	private final UserService userService;
	private final TaskScheduler taskScheduler;
	private final SimpMessagingTemplate template;

	@Autowired
	public ApplicationController(UserService userService, TaskScheduler taskScheduler, SimpMessagingTemplate template) {
		this.userService = userService;
		this.taskScheduler = taskScheduler;
		this.template = template;
	}
	
	// TODO: change to queue up to detection process
	@SubscribeMapping("/{userId}")
	public void subscribeTrigger(@DestinationVariable String userId) {
		System.err.println("user with id: " + userId + "subscribed.");
	}
	
	@MessageMapping("/start-detection/{userId}")
	@SendTo("/user-progress/{userId}")
	public Map<String, Object> startDetectionProcess(@DestinationVariable String userId) throws InterruptedException {
		System.err.println("startDetectionProcess called..." + userId);
		Map<String, Object> response = new HashMap<>();
		// initiate detection ...
		User user = userService.findById(userId);
		if (user == null) {
			response.put("status", "failed");
			return response;
		}
		if (scheduledFuture != null) {
			scheduledFuture.cancel(false);
		}
		Thread.sleep(1000);
		scheduledFuture = taskScheduler.scheduleAtFixedRate( () -> {
			detectionProgress(userId);
		}, 30);
		response.put("status","ok");
		return response;
	}
	
	
	public void updateProgressInfo(@DestinationVariable String userId, int progress) {
		Map<String, Object> response = new HashMap<>();
		response.put("progress", progress);
		this.template.convertAndSend("/user-progress/"+userId, response);
	}
	
	private void detectionProgress(String userId) {
		if (dummyProgress == null) {
			dummyProgress = 0.;
		}
		updateProgressInfo(userId, (int) Math.round(dummyProgress*100));
		dummyProgress += 0.01;
		if (dummyProgress > 1.005) {
			dummyProgress = null;
			scheduledFuture.cancel(false);
		}
	}
	
}
