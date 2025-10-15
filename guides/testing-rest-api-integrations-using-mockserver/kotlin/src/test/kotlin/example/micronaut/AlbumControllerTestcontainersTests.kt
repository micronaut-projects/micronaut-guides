package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.wiremock.integrations.testcontainers.WireMockContainer

@Testcontainers(disabledWithoutDocker = true) // <1>
class AlbumControllerTestcontainersTests {

    companion object {
        @Container // <2>
        @JvmField
        var wiremockServer: WireMockContainer = WireMockContainer("wiremock/wiremock:2.35.0")
            .withMapping("photos-by-album", AlbumControllerTestcontainersTests::class.java, "mocks-config.json") // <3>
            .withFileFromResource(
                "album-photos-response.json",
                AlbumControllerTestcontainersTests::class.java,
                "album-photos-response.json"
            )
    }

    private fun getProperties(): Map<String, Any> { // <4>
        return mapOf("micronaut.http.services.photosapi.url" to wiremockServer.baseUrl) // <5>
    }

    @Test
    fun shouldGetAlbumById() {
        val albumId = 1L
        ApplicationContext.run(EmbeddedServer::class.java, getProperties()).use { server ->
            RestAssured.port = server.port

            RestAssured.given().contentType(ContentType.JSON)
                .`when`()
                .get("/api/albums/{albumId}", albumId)
                .then()
                .statusCode(200)
                .body("albumId", `is`(albumId.toInt()))
                .body("photos", hasSize<Int>(2))
        }
    }

    @Test
    fun shouldReturnServerErrorWhenPhotoServiceCallFailed() {
        val albumId = 2L
        ApplicationContext.run(EmbeddedServer::class.java, getProperties()).use { server ->
            RestAssured.port = server.port
            RestAssured.given().contentType(ContentType.JSON)
                .`when`()
                .get("/api/albums/{albumId}", albumId)
                .then()
                .statusCode(500)
        }
    }

    @Test
    fun shouldReturnEmptyPhotos() {
        val albumId = 3L
        ApplicationContext.run(EmbeddedServer::class.java, getProperties()).use { server ->
            RestAssured.port = server.port
            RestAssured.given().contentType(ContentType.JSON)
                .`when`()
                .get("/api/albums/{albumId}", albumId)
                .then()
                .statusCode(200)
                .body("albumId", `is`(albumId.toInt()))
                .body("photos", nullValue())
        }
    }
}