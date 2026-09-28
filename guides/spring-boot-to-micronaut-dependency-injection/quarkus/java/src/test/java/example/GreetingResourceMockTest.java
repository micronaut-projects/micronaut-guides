package example;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
class GreetingResourceMockTest {
    @InjectMock
    GreetingService greetingService;

    @Test
    void testHelloEndpoint() {
        Mockito.when(greetingService.greet()).thenReturn("Hello Quarkus");
        given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(is("Hello Quarkus"));
        Mockito.verify(greetingService).greet();
        Mockito.verifyNoMoreInteractions(greetingService);
    }
}
