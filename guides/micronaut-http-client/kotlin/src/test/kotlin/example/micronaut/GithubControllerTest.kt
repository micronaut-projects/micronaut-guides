package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Requires
import io.micronaut.core.io.ResourceLoader
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.StreamingHttpClient
import io.micronaut.runtime.server.EmbeddedServer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.io.BufferedReader
import java.io.InputStream
import java.util.*
import java.util.regex.Pattern

class GithubControllerTest {
    val MICRONAUT_RELEASE =
            Pattern.compile("[Micronaut|Micronaut Framework] [0-9].[0-9].[0-9]([0-9])?( (RC|M)[0-9])?")

    @Test
    fun verifyGithubReleasesCanBeFetchedWithLowLevelHttpClient() {
        val config = mapOf("micronaut.codec.json.additional-types" to listOf("application/vnd.github.v3+json"),
            "spec.name" to "GithubControllerTest")  // <1>
        val github : EmbeddedServer = ApplicationContext.run(EmbeddedServer::class.java, config)
        val embeddedServer : EmbeddedServer  = ApplicationContext.run(EmbeddedServer::class.java,
            mapOf("micronaut.http.services.github.url" to "http://localhost:${github.port}")
        ) // <2>
        val httpClient : HttpClient = embeddedServer.applicationContext
            .createBean(HttpClient::class.java, embeddedServer.url)
        val client = httpClient.toBlocking()
        assertReleases(client, "/github/releases")
        assertReleases(client, "/github/releases-lowlevel")
        httpClient.close()
        embeddedServer.close()
        github.close()
    }

    fun assertReleases(client: BlockingHttpClient, path: String) {
        val request : HttpRequest<Any> = HttpRequest.GET(path)
        val rsp = client.exchange(request, // <3>
            Argument.listOf(GithubRelease::class.java)) // <4>
        assertEquals(HttpStatus.OK, rsp.status)   // <5>
        val releases = rsp.body()
        assertNotNull(releases)
        assertReleases(releases.toList()) // <6>
    }

    fun assertReleases(releases: List<GithubRelease>) {
        assertNotNull(releases)
        Assertions.assertTrue(releases
            .map { it.name }
            .all { MICRONAUT_RELEASE.matcher(it).find() })
    }

    @Requires(property = "spec.name", value = "GithubControllerTest") // <1>
    @Controller
    class GithubReleases(val resourceLoader : ResourceLoader) {
        @Produces("application/vnd.github.v3+json")
        @Get("/repos/micronaut-projects/micronaut-core/releases")
        fun coreReleases() : Optional<String> {
            return resourceLoader.getResourceAsStream("releases.json").map(this::inputStreamToString)
        }

        fun inputStreamToString(inputStream: InputStream) : String {
            val reader = BufferedReader(inputStream.reader())
            var content: String
            try {
                content = reader.readText()
            } finally {
                reader.close()
            }
            return content
        }
    }

}
