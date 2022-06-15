package example.micronaut;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

@Component // <1>
public class GreetingJob {
    private static final Logger LOG = LoggerFactory.getLogger(GreetingJob.class);

    private final GreetingService greetingService;

    public GreetingJob(GreetingService greetingService) { // <2>
        this.greetingService = greetingService;
    }

    @Scheduled(fixedDelayString = "30s") // <3>
    void printLastGreeting() {
        final Optional<Greeting> lastGreeting = greetingService.getLastGreeting();
        lastGreeting.ifPresent(greeting -> {
            LOG.info("Last Greeting was = {}", greeting.getContent());
        });
    }
}
