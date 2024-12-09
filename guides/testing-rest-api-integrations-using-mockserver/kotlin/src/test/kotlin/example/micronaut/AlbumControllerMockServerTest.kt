package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockserver.client.MockServerClient
import org.mockserver.model.Header
import org.mockserver.model.JsonBody.json
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response
import org.mockserver.verify.VerificationTimes
import org.testcontainers.containers.MockServerContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.util.Collections
import java.util.Map

@MicronautTest // <1>
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // <2>
@Testcontainers(disabledWithoutDocker = true) // <3>
class AlbumControllerMockServerTest : TestPropertyProvider { // <4>

    @Container
    val mockServerContainer = MockServerContainer(DockerImageName.parse("mockserver/mockserver:5.15.0"))

    private lateinit var mockServerClient: MockServerClient

    override fun getProperties(): @NonNull Map<String, String> { // <4>
        mockServerContainer.start()
        mockServerClient = MockServerClient(
            mockServerContainer.host,
            mockServerContainer.serverPort
        )
        return Collections.singletonMap(
            "micronaut.http.services.photosapi.url", // <5>
            mockServerContainer.endpoint
        )
    }

    @BeforeEach
    fun setUp() {
        mockServerClient.reset()
    }

    @Test
    fun shouldGetAlbumById(spec: RequestSpecification) { // <6>
        val albumId = 1L

        mockServerClient
            .when(request().withMethod("GET").withPath("/albums/$albumId/photos"))
            .respond(
                response()
                    .withStatusCode(200)
                    .withHeaders(Header("Content-Type", "application/json; charset=utf-8"))
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
            )

        spec  // <7>
            .contentType(ContentType.JSON)
            .`when`()
            .get("/api/albums/{albumId}", albumId)
            .then()
            .statusCode(200)
            .body("albumId", `is`(albumId.toInt()))
            .body("photos", hasSize(2))

        verifyMockServerRequest("GET", "/albums/$albumId/photos", 1)
    }

    private fun verifyMockServerRequest(method: String, path: String, times: Int) {
        mockServerClient.verify(
            request().withMethod(method).withPath(path),
            VerificationTimes.exactly(times)
        )
    }
}
