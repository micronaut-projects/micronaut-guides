package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Requires
import io.micronaut.core.io.ResourceLoader
import io.micronaut.http.HttpRequest
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.StreamingHttpClient
import io.micronaut.runtime.server.EmbeddedServer
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux

import java.io.InputStream
import java.util.Optional
import java.util.regex.Pattern
import java.util.stream.Stream

class GithubDeclarativeControllerTest {
    val MICRONAUT_RELEASE =
        Pattern.compile("[Micronaut|Micronaut Framework] [0-9].[0-9].[0-9]([0-9])?( (RC|M)[0-9])?")

    @Test
    fun verifyGithubReleasesCanBeFetchedWithLowLevelHttpClient() {

        val github : EmbeddedServer = ApplicationContext.run(EmbeddedServer::class.java,
                mapOf("spec.name" to "GithubDeclarativeControllerTest")
        ) // <1>
        val embeddedServer : EmbeddedServer  = ApplicationContext.run(EmbeddedServer::class.java,
                mapOf("micronaut.http.services.github.url" to "http://localhost:${github.port}")
        ) // <2>
        val streamingClient : StreamingHttpClient = embeddedServer.applicationContext
            .createBean(StreamingHttpClient::class.java, embeddedServer.url)
        val request : HttpRequest<Any> = HttpRequest.GET("/github/releases")
        val githubReleases = Flux.from(streamingClient.jsonStream(request, GithubRelease::class.java)).toStream()
        assertReleases(githubReleases)
        streamingClient.close()
        embeddedServer.close()
        github.close()
    }

    fun assertReleases(releases: Stream<GithubRelease>) {
        assertTrue(releases
            .map { it.name }
            .allMatch { MICRONAUT_RELEASE.matcher(it).find() })
    }

    @Requires(property = "spec.name", value = "GithubDeclarativeControllerTest") // <1>
    @Controller
    class GithubReleases(val resourceLoader: ResourceLoader) {
        @Get("/repos/micronaut-projects/micronaut-core/releases")
        fun coreReleases() : Optional<InputStream>  {
            return resourceLoader.getResourceAsStream("releases.json");
        }
    }
}