package example.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.views.View;

import java.util.Collections;
import java.util.Map;

@Controller
public class GreetingController {

	@Get("/greeting")
	@View("greeting.html")
	public Map<String, Object> greetingForm() {
		return Collections.singletonMap("greeting", new Greeting());
	}

	@Post("/greeting")
	@View("result.html")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Map<String, Object> greetingSubmit(@Body Greeting greeting) {
		return Collections.singletonMap("greeting", greeting);
	}

}
