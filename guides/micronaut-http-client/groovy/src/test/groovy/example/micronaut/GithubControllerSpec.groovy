package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Requires
import io.micronaut.core.io.ResourceLoader
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.Ignore
import spock.lang.Specification
import java.util.regex.Pattern

class GithubControllerSpec extends Specification {
    private static Pattern MICRONAUT_RELEASE =
            Pattern.compile("[Micronaut|Micronaut Framework] [0-9].[0-9].[0-9]([0-9])?( (RC|M)[0-9])?")

    @Ignore
    void "verify GithubReleases can Be fetched With Low Level Http Client"() {
        given:
        EmbeddedServer github = ApplicationContext.run(EmbeddedServer,
                Collections.singletonMap("spec.name", "GithubControllerTest")) // <1>
        String url = "http://localhost:${github.port}"
        EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer,
                Collections.singletonMap("micronaut.http.services.github.url", url)) // <2>
        HttpClient httpClient = embeddedServer.getApplicationContext()
                .createBean(HttpClient, embeddedServer.getURL())
        BlockingHttpClient client = httpClient.toBlocking()

        when:
        HttpRequest<?> request = HttpRequest.GET("/github/releases-lowlevel")
        HttpResponse<List<GithubRelease>> rsp = client.exchange(request, // <3>
                Argument.listOf(GithubRelease)) // <4>

        then:
        HttpStatus.OK == rsp.getStatus()   // <5>

        when:
        List<GithubRelease> releases = rsp.body() // <6>

        then:
        releases
        releases.stream()
                .map(GithubRelease::getName)
                .allMatch(name -> MICRONAUT_RELEASE.matcher(name)
                        .find())

        cleanup:
        httpClient.close()
        embeddedServer.close()
        github.close()
    }

    @Requires(property = "spec.name", value = "GithubControllerTest") // <1>
    @Controller
    static class GithubReleases {
        private final ResourceLoader resourceLoader
        GithubReleases(ResourceLoader resourceLoader) {
            this.resourceLoader = resourceLoader
        }

        @Consumes(value = ["application/vnd.github.v3+json", "application/json"])
        @Get("/repos/micronaut-projects/micronaut-core/releases") // <3>
        String coreReleases() {
            resourceLoader.getResourceAsStream("releases.json")
                    .map { it.text }
                    .orElseGet(() -> '[]')
        }
    }
}
