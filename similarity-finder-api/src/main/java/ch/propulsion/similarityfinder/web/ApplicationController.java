package ch.propulsion.similarityfinder.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.bind.annotation.RestController;

import ch.propulsion.similarityfinder.domain.User;
import ch.propulsion.similarityfinder.service.detection.DocumentComparator;
import ch.propulsion.similarityfinder.service.entity.UserService;

@RestController
public class ApplicationController {
	
	private Double dummyProgress = null;
	private ScheduledFuture<?> scheduledFuture = null;
	private final UserService userService;
	private final TaskScheduler taskScheduler;
	private final SimpMessagingTemplate template;
	private final DocumentComparator comparator;

	@Autowired
	public ApplicationController(UserService userService, TaskScheduler taskScheduler, SimpMessagingTemplate template, 
			DocumentComparator comparator) {
		this.userService = userService;
		this.taskScheduler = taskScheduler;
		this.template = template;
		this.comparator = comparator;
	}
	
	@MessageMapping("/start-detection/{userId}")
	@SendTo("/user-progress/{userId}")
	public Map<String, Object> startDetectionProcess(@DestinationVariable String userId) throws InterruptedException {
		Map<String, Object> response = new HashMap<>();
		User user = userService.findById(userId);
		if (user == null) {
			response.put("status", "failed");
			return response;
		}
		taskScheduler.schedule(() -> {
			System.out.println("Starting detection for user: " + userId);
			comparator.initiateDetection(userId);
		}, new Date());
		response.put("status","ok");
		return response;
	}
	
	public void updateProcessDone(String userId) {
		Map<String, Object> response = new HashMap<>();
		response.put("status", "ok");
		response.put("progress", 100);
		response.put("isDone", true);
		this.template.convertAndSend("/user-progress/"+userId, response);
	}
	
	
	public void updateProgressInfo(@DestinationVariable String userId, int progress) {
		Map<String, Object> response = new HashMap<>();
		response.put("status", "ok");
		response.put("progress", progress);
		response.put("isDone", false);
		this.template.convertAndSend("/user-progress/"+userId, response);
	}
	
	/*
	 *  Dummy Controls...
	 */
	
	@MessageMapping("/start-dummy-detection/{userId}")
	@SendTo("/user-progress/{userId}")
	public Map<String, Object> startDummyDetectionProcess(@DestinationVariable String userId) throws InterruptedException {
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
			dummyDetectionProgress(userId);
		}, 30);
		response.put("status","ok");
		return response;
	}
	
	private void dummyDetectionProgress(String userId) {
		if (dummyProgress == null) {
			dummyProgress = 0.;
		}
		updateProgressInfo(userId, (int) Math.round(dummyProgress*100));
		dummyProgress += 0.01;
		if (dummyProgress > 1.005) {
			dummyProgress = null;
			scheduledFuture.cancel(false);
			updateProcessDone(userId);
		}
	}
	
}
