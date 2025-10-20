package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.core.annotation.NonNull
import io.micronaut.runtime.server.EmbeddedServer
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.wiremock.integrations.testcontainers.WireMockContainer
import java.util.Collections

@Testcontainers(disabledWithoutDocker = true) // <1>
class AlbumControllerTestcontainersTests {

    @Container // <2>
    @JvmField
    val wiremockServer = WireMockContainer("wiremock/wiremock:2.35.0")
        .withMapping("photos-by-album", AlbumControllerTestcontainersTests::class.java, "mocks-config.json") // <3>
        .withFileFromResource(
            "album-photos-response.json",
            AlbumControllerTestcontainersTests::class.java,
            "album-photos-response.json"
        )

    @NonNull
    fun getProperties(): Map<String, Any> { // <4>
        return Collections.singletonMap("micronaut.http.services.photosapi.url", // <5>
            wiremockServer.baseUrl)
    }

    @Test
    fun shouldGetAlbumById() {
        val albumId = 1L
        EmbeddedServer.create(ApplicationContext.run(EmbeddedServer::class.java, getProperties())).use { server ->
            RestAssured.port = server.port

            given().contentType(ContentType.JSON)
                .`when`()
                .get("/api/albums/{albumId}", albumId)
                .then()
                .statusCode(200)
                .body("albumId", is(albumId.toInt()))
            .body("photos", hasSize(2))
        }
    }

    @Test
    fun shouldReturnServerErrorWhenPhotoServiceCallFailed() {
        val albumId = 2L
        EmbeddedServer.create(ApplicationContext.run(EmbeddedServer::class.java, getProperties())).use { server ->
            RestAssured.port = server.port
            given().contentType(ContentType.JSON)
                .`when`()
                .get("/api/albums/{albumId}", albumId)
                .then()
                .statusCode(500)
        }
    }

    @Test
    fun shouldReturnEmptyPhotos() {
        val albumId = 3L
        EmbeddedServer.create(ApplicationContext.run(EmbeddedServer::class.java, getProperties())).use { server ->
            RestAssured.port = server.port
            given().contentType(ContentType.JSON)
                .`when`()
                .get("/api/albums/{albumId}", albumId)
                .then()
                .statusCode(200)
                .body("albumId", is(albumId.toInt()))
            .body("photos", nullValue())
        }
    }
}
