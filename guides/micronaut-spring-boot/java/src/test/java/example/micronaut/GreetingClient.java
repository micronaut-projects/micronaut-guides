package example.micronaut;

import io.micronaut.http.client.annotation.Client;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import io.micronaut.core.annotation.Nullable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Client("/") // <1>
public interface GreetingClient {

    @GetMapping("/greeting{?name}") // <2>
    Greeting greet(@Nullable String name);

    @PostMapping("/greeting") // <3>
    Greeting greetByPost(@RequestBody Greeting greeting); // <4>

    @DeleteMapping("/greeting") // <5>
    HttpStatus deletePost();

    @GetMapping("/nested/greeting{?name}") // <2>
    Greeting nestedGreet(@RequestParam @Nullable String name); // <6>

    @GetMapping("/greeting-status{?name}") // <2>
    ResponseEntity<Greeting> greetWithStatus(@RequestParam @Nullable String name); // <6>
}
