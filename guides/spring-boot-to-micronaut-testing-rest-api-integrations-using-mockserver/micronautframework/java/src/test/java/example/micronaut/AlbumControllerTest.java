package example.micronaut;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.JsonBody.json;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.Header;
import org.mockserver.verify.VerificationTimes;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Collections;
import java.util.Map;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Testcontainers(disabledWithoutDocker = true)
class AlbumControllerTest implements TestPropertyProvider {

    @Container
    static MockServerContainer mockServerContainer = new MockServerContainer(
            DockerImageName.parse("mockserver/mockserver:5.15.0")
    );

    static MockServerClient mockServerClient;


    @Override
    public @NonNull Map<String, String> getProperties() {
        mockServerContainer.start();
        mockServerClient =
                new MockServerClient(
                        mockServerContainer.getHost(),
                        mockServerContainer.getServerPort()
                );
        return Collections.singletonMap("micronaut.http.services.jsonplaceholder.url", mockServerContainer.getEndpoint());
    }

    @BeforeEach
    void setUp() {
        mockServerClient.reset();
    }

    @Test
    void shouldGetAlbumById(RequestSpecification spec) {
        Long albumId = 1L;

        mockServerClient
                .when(request().withMethod("GET").withPath("/albums/" + albumId + "/photos"))
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeaders(new Header("Content-Type", "application/json; charset=utf-8"))
                                .withBody(
                                        json(
                                                """
                                                [
                                                     {
                                                         "id": 1,
                                                         "title": "accusamus beatae ad facilis cum similique qui sunt",
                                                         "url": "https://via.placeholder.com/600/92c952",
                                                         "thumbnailUrl": "https://via.placeholder.com/150/92c952"
                                                     },
                                                     {
                                                         "id": 2,
                                                         "title": "reprehenderit est deserunt velit ipsam",
                                                         "url": "https://via.placeholder.com/600/771796",
                                                         "thumbnailUrl": "https://via.placeholder.com/150/771796"
                                                     }
                                                 ]
                                                """
                                        )
                                )
                );
        spec
                .contentType(ContentType.JSON)
                .when()
                .get("/api/albums/{albumId}", albumId)
                .then()
                .statusCode(200)
                .body("albumId", is(albumId.intValue()))
                .body("photos", hasSize(2));

        verifyMockServerRequest("GET", "/albums/" + albumId + "/photos", 1);
    }

    private void verifyMockServerRequest(String method, String path, int times) {
        mockServerClient.verify(
                request().withMethod(method).withPath(path),
                VerificationTimes.exactly(times)
        );
    }
}