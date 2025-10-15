package example.micronaut

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.github.tomakehurst.wiremock.junit5.WireMockExtension
import io.micronaut.context.ApplicationContext
import io.micronaut.http.MediaType
import io.micronaut.runtime.server.EmbeddedServer
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers(disabledWithoutDocker = true) // <1>
class AlbumControllerTest {

    companion object {
        @JvmField
        @RegisterExtension // <2>
        var wireMock: WireMockExtension = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build()
    }

    private fun getProperties(): Map<String, Any> {
        return mapOf("micronaut.http.services.photosapi.url" to wireMock.baseUrl()) // <3>
    }

    @Test
    fun shouldGetAlbumById() { // <4>
        ApplicationContext.run(EmbeddedServer::class.java, getProperties()).use { server ->
            RestAssured.port = server.port // <5>
            val albumId = 1L
            wireMock.stubFor( // <6>
                WireMock.get(WireMock.urlMatching("/albums/$albumId/photos"))
                    .willReturn(
                        WireMock.aResponse()
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
                                """
                            )
                    )
            )

            RestAssured.given().contentType(ContentType.JSON)
                .`when`()
                .get("/api/albums/{albumId}", albumId)
                .then()
                .statusCode(200)
                .body("albumId", `is`(albumId.toInt()))
                .body("photos", hasSize<Int>(2))
        }
    }

    @Test  // <7>
    fun shouldReturnServerErrorWhenPhotoServiceCallFailed() {
        ApplicationContext.run(EmbeddedServer::class.java, getProperties()).use { server ->
            RestAssured.port = server.port // <5>
            val albumId = 2L
            wireMock.stubFor(
                WireMock.get(WireMock.urlMatching("/albums/$albumId/photos"))
                    .willReturn(WireMock.aResponse().withStatus(500))
            )

            RestAssured.given().contentType(ContentType.JSON)
                .`when`()
                .get("/api/albums/{albumId}", albumId)
                .then()
                .statusCode(500)
        }
    }
}