package example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GreetingServiceTest {

    @Test
    void getGreetingOk() {
        assertEquals("Hello World!", new GreetingService(new GreetingConfiguration() {
            @Override
            public String getMessage() {
                return "Hello";
            }

            @Override
            public String getSuffix() {
                return "!";
            }

            @Override
            public Optional<String> getName() {
                return Optional.of("World");
            }

            @Override
            public Optional<ContentConfig> getContent() {
                return Optional.empty();
            }
        }).greet());
    }
}