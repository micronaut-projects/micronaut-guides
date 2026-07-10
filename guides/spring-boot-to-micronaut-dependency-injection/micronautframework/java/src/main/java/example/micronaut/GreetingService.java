package example.micronaut;

import jakarta.inject.Singleton;

@Singleton
public class GreetingService {
    private final GreetingConfiguration greetingConfiguration;

    public GreetingService(GreetingConfiguration greetingConfiguration) {
        this.greetingConfiguration = greetingConfiguration;
    }

    public String greet() {
        return greetingConfiguration.getMessage() + " " + greetingConfiguration.getName() + greetingConfiguration.getSuffix();
    }
}
