package example.micronaut;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest // <1>
public class BasicAuthClientTest {

    @Inject
    AppClient appClient; // <2>

    @Test
    void verifyBasicAuthWorks() {
        String credsEncoded = Base64.getEncoder().encodeToString("sherlock:password".getBytes());
        String rsp = appClient.home("Basic " + credsEncoded); // <3>
        assertEquals("sherlock", rsp);
    }
}
