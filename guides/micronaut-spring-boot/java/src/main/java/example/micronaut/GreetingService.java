package example.micronaut;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Pattern;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Service // <1>
@Validated // <2>
public class GreetingService {

    private final AtomicLong counter = new AtomicLong();

    private final GreetingConfiguration greetingConfiguration;

    private final AtomicReference<Greeting> lastGreeting = new AtomicReference<>();

    public GreetingService(GreetingConfiguration greetingConfiguration) { // <3>
        this.greetingConfiguration = greetingConfiguration;
    }

    public Greeting greeting(@Pattern(regexp = "\\D+") String name) { // <4>
        final Greeting greeting = new Greeting(counter.incrementAndGet(),
                String.format(greetingConfiguration.getTemplate(), name));
        lastGreeting.set(greeting);
        return greeting;
    }

    public Optional<Greeting> getLastGreeting() {
        return Optional.ofNullable(lastGreeting.get());
    }
}
