package controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class PublicController {
	
	@RequestMapping(method = GET, value = "/foo")
	public Map<String, String> getInfo() {
		Map<String, String> infoObj = new HashMap<>();
		infoObj.put("hello", "say");
		return infoObj;
		
	}

}
