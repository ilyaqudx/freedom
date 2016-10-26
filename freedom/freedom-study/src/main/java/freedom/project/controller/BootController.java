package freedom.project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BootController {

	@RequestMapping("/start")
	@ResponseBody
	public String start()
	{
		return "hello world";
	}
}
