package example.micronaut

import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.github.tomakehurst.wiremock.junit5.WireMockExtension
import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers(disabledWithoutDocker = true) // <1>
class AlbumControllerWireMockMappingTests {

    companion object {
        //tag::registerExtension[]
        @JvmField
        @RegisterExtension
        var wireMockServer: WireMockExtension = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort().usingFilesUnderClasspath("wiremock"))
            .build()
        //end::registerExtension[]
    }

    private fun getProperties(): Map<String, Any> {
        return mapOf("micronaut.http.services.photosapi.url" to wireMockServer.baseUrl()) //
    }

    //tag::shouldGetAlbumById[]
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
    //end::shouldGetAlbumById[]

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