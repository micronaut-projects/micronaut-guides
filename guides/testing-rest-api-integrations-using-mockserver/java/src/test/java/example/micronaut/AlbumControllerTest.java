package example.micronaut;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import io.micronaut.context.ApplicationContext;
import io.micronaut.http.MediaType;
import io.micronaut.runtime.server.EmbeddedServer;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;

@Testcontainers(disabledWithoutDocker = true) // <1>
class AlbumControllerTest {

    @RegisterExtension // <2>
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    private Map<String, Object> getProperties() {
        return Collections.singletonMap("micronaut.http.services.photosapi.url", // <3>
                wireMock.baseUrl());
    }

    @Test
    void shouldGetAlbumById() { // <4>
        try (EmbeddedServer server = ApplicationContext.run(EmbeddedServer.class, getProperties())) {
            RestAssured.port = server.getPort(); // <5>
            Long albumId = 1L;
            wireMock.stubFor( // <6>
                    WireMock.get(urlMatching("/albums/" + albumId + "/photos"))
                            .willReturn(
                                    aResponse()
                                            .withHeader("Content-Type", MediaType.APPLICATION_JSON)
                                            .withBody(
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
                                                            """)));

            given().contentType(ContentType.JSON)
                    .when()
                    .get("/api/albums/{albumId}", albumId)
                    .then()
                    .statusCode(200)
                    .body("albumId", is(albumId.intValue()))
                    .body("photos", hasSize(2));
        }
    }

    @Test  // <7>
    void shouldReturnServerErrorWhenPhotoServiceCallFailed() {
        try (EmbeddedServer server = ApplicationContext.run(EmbeddedServer.class, getProperties())) {
            RestAssured.port = server.getPort(); // <5>
            Long albumId = 2L;
            wireMock.stubFor(WireMock.get(urlMatching("/albums/" + albumId + "/photos"))
                    .willReturn(aResponse().withStatus(500)));

            given().contentType(ContentType.JSON)
                    .when()
                    .get("/api/albums/{albumId}", albumId)
                    .then()
                    .statusCode(500);
        }
    }
}
