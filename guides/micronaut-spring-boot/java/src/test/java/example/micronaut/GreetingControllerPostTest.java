package example.micronaut;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
public class GreetingControllerPostTest {

    @Inject
    GreetingClient greetingClient;

    @Test
    void postWithRequestBodyAndPostMappingWorks() {
        Greeting request = new Greeting(99l, "Sergio");
        Greeting response = greetingClient.greetByPost(request) ;
        assertNotNull(response);
        assertNotEquals(request.getId(), response.getId());
        assertEquals("Hola, " + request.getContent() + "!", response.getContent());
    }
}
