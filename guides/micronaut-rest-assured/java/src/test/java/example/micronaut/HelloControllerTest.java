package example.micronaut;

import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@MicronautTest // <1>
public class HelloControllerTest {

    @Inject
    EmbeddedServer embeddedServer; // <2>

    @Test
    public void testHelloEndpoint() {
        given().
                port(embeddedServer.getPort()). // <3>
        when().
                get("/hello").
        then().
                statusCode(200).
                body(is("Hello World"));
    }

}