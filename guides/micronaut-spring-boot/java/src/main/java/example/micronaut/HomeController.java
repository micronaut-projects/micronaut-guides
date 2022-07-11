package example.micronaut;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // <1>
public class HomeController {
    @GetMapping(path = "/", produces = "text/html") // <2>
    public String home(Model model) { // <3>
        model.addAttribute(
                "message",
                "Welcome to Micronaut for Spring");

        return "home"; // <4>
    }
}
