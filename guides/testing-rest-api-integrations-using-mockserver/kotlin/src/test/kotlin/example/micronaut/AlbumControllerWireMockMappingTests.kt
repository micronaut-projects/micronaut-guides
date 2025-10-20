package example.micronaut

import com.github.tomakehurst.wiremock.junit5.WireMockExtension
import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.Collections

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import static io.restassured.RestAssured.given
import static org.hamcrest.CoreMatchers.`is`
import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.Matchers.nullValue

@Testcontainers(disabledWithoutDocker = true) // <1>
class AlbumControllerWireMockMappingTests {

    //tag::registerExtension[]
    @RegisterExtension
    @JvmField
    val wireMockServer = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort().usingFilesUnderClasspath("wiremock"))
        .build()
    //end::registerExtension[]

    private fun getProperties(): Map<String, Any> { // <5>
        return Collections.singletonMap("micronaut.http.services.photosapi.url", wireMockServer.baseUrl())
    }

    //tag::shouldGetAlbumById[]
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
                .body("albumId", `is`(albumId.toInt()))
                .body("photos", hasSize(2))
        }
    }
    //end::shouldGetAlbumById[]

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
                .body("albumId", `is`(albumId.toInt()))
                .body("photos", nullValue())
        }
    }
}
