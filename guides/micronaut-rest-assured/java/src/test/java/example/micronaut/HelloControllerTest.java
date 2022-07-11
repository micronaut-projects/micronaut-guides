package example.micronaut;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;

@MicronautTest // <1>
public class HelloControllerTest {

    @Test
    public void testHelloEndpoint(RequestSpecification spec) { // <2>
        spec    // <3>
            .when()
                .get("/hello")
            .then()
                .statusCode(200)
                .body(is("Hello World"));
    }
}