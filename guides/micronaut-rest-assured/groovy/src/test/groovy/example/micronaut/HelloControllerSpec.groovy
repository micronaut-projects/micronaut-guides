package example.micronaut

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.restassured.specification.RequestSpecification
import jakarta.inject.Inject
import spock.lang.Specification

import static org.hamcrest.CoreMatchers.is

@MicronautTest // <1>
class HelloControllerSpec extends Specification {

    @Inject
    RequestSpecification spec // <2>

    void "test hello endpoint"() {
        expect:
        spec    // <3>
            .when()
                .get('/hello')
            .then()
                .statusCode(200)
                .body(is('Hello World'))
    }
}
