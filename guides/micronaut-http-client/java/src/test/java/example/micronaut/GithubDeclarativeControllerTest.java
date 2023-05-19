package example.micronaut;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.StreamingHttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.io.InputStream;
import java.util.Collections;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GithubDeclarativeControllerTest {

    private static Pattern MICRONAUT_RELEASE =
            Pattern.compile("[Micronaut|Micronaut Framework] [0-9].[0-9].[0-9]([0-9])?( (RC|M)[0-9])?");

    @Test
    void verifyGithubReleasesCanBeFetchedWithLowLevelHttpClient() {
        EmbeddedServer github = ApplicationContext.run(EmbeddedServer.class,
                Collections.singletonMap("spec.name", "GithubDeclarativeControllerTest")); // <1>
        EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer.class,
                Collections.singletonMap("micronaut.http.services.github.url",
                        "http://localhost:" + github.getPort())); // <2>
        StreamingHttpClient streamingClient = embeddedServer.getApplicationContext()
                .createBean(StreamingHttpClient.class, embeddedServer.getURL());
        HttpRequest<Object> request = HttpRequest.GET("/github/releases");
        Stream<GithubRelease> githubReleases = Flux.from(streamingClient.jsonStream(request, GithubRelease.class)).toStream();
        assertReleases(githubReleases);
        streamingClient.close();
        embeddedServer.close();
        github.close();
    }

    private static void assertReleases(Stream<GithubRelease> releases) {
        assertTrue(releases
                .map(GithubRelease::getName)
                .allMatch(name -> MICRONAUT_RELEASE.matcher(name).find()));
    }

    @Requires(property = "spec.name", value = "GithubDeclarativeControllerTest") // <1>
    @Controller
    static class GithubReleases {
        private final ResourceLoader resourceLoader;
        GithubReleases(ResourceLoader resourceLoader) {
            this.resourceLoader = resourceLoader;
        }

        @Get("/repos/micronaut-projects/micronaut-core/releases")
        Optional<InputStream> coreReleases() {
            return resourceLoader.getResourceAsStream("releases.json");
        }
    }
}