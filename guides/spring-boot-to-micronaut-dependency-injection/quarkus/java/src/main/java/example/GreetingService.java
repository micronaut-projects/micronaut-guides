package example;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GreetingService {
    private final GreetingConfiguration greetingConfiguration;

    public GreetingService(GreetingConfiguration greetingConfiguration) {
        this.greetingConfiguration = greetingConfiguration;
    }

    public String greet() {
        return greetingConfiguration.getMessage() + " " + greetingConfiguration.getName().orElse("") + greetingConfiguration.getSuffix();
    }
}
