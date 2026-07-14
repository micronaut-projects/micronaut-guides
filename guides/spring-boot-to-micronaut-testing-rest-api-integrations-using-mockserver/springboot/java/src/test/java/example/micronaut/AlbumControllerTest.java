package example.micronaut;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.JsonBody.json;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.Header;
import org.mockserver.verify.VerificationTimes;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class AlbumControllerTest {

    @LocalServerPort
    private Integer port;

    @Container
    static MockServerContainer mockServerContainer = new MockServerContainer(
            DockerImageName.parse("mockserver/mockserver:5.15.0")
    );

    static MockServerClient mockServerClient;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        mockServerClient =
                new MockServerClient(
                        mockServerContainer.getHost(),
                        mockServerContainer.getServerPort()
                );
        registry.add("photos.api.base-url", mockServerContainer::getEndpoint);
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        mockServerClient.reset();
    }

    @Test
    void shouldGetAlbumById() {
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

        given()
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