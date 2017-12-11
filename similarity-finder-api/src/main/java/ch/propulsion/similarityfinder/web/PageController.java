package ch.propulsion.similarityfinder.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
	
	@RequestMapping("/")
	public String loadPage() {
		return "index.html";
	}
	
}
