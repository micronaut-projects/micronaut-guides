package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Requires
import io.micronaut.core.io.ResourceLoader
import io.micronaut.http.HttpRequest
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.StreamingHttpClient
import io.micronaut.runtime.server.EmbeddedServer
import reactor.core.publisher.Flux
import spock.lang.Specification
import java.util.regex.Pattern
import java.util.stream.Stream

class GithubDeclarativeControllerSpec extends Specification {

    private static Pattern MICRONAUT_RELEASE =
            Pattern.compile("[Micronaut|Micronaut Framework] [0-9].[0-9].[0-9]([0-9])?( (RC|M)[0-9])?")

    void "verify GitHub releases can be fetched with Streaming HttpClient"() {
        given:
        EmbeddedServer github = ApplicationContext.run(EmbeddedServer,
                Collections.singletonMap("spec.name", "GithubDeclarativeControllerTest")) // <1>
        EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer,
                Collections.singletonMap("micronaut.http.services.github.url",
                        "http://localhost:" + github.getPort())) // <2>
        StreamingHttpClient streamingClient = embeddedServer.getApplicationContext()
                .createBean(StreamingHttpClient, embeddedServer.getURL())

        when:
        HttpRequest<Object> request = HttpRequest.GET("/github/releases")
        Stream<GithubRelease> githubReleases = Flux.from(streamingClient.jsonStream(request, GithubRelease)).toStream()

        then:
        githubReleases
        githubReleases
                .map(GithubRelease::getName)
                .allMatch(name -> MICRONAUT_RELEASE.matcher(name).find())

        cleanup:
        streamingClient.close()
        embeddedServer.close()
        github.close()
    }

    @Requires(property = "spec.name", value = "GithubDeclarativeControllerTest") // <1>
    @Controller
    static class GithubReleases {
        private final ResourceLoader resourceLoader
        GithubReleases(ResourceLoader resourceLoader) {
            this.resourceLoader = resourceLoader
        }

        @Get("/repos/micronaut-projects/micronaut-core/releases")
        Optional<InputStream> coreReleases() {
            resourceLoader.getResourceAsStream("releases.json")
        }
    }
}