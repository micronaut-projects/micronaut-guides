/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.micronaut;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static java.nio.charset.StandardCharsets.UTF_8;

class GithubControllerTest {
    private static Pattern MICRONAUT_RELEASE =
            Pattern.compile("Micronaut (Core |Framework )?v?\\d+.\\d+.\\d+( (RC|M)\\d)?");

    @Test
    void verifyGithubReleasesCanBeFetchedWithLowLevelHttpClient() {
        EmbeddedServer github = ApplicationContext.run(EmbeddedServer.class,
                Map.of("micronaut.codec.json.additional-types", "application/vnd.github.v3+json",
                        "spec.name", "GithubControllerTest")); // <1>
        EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer.class,
                Collections.singletonMap("micronaut.http.services.github.url",
                        "http://localhost:" + github.getPort())); // <2>
        HttpClient httpClient = embeddedServer.getApplicationContext()
                .createBean(HttpClient.class, embeddedServer.getURL());
        BlockingHttpClient client = httpClient.toBlocking();
        assertReleases(client, "/github/releases");
        assertReleases(client, "/github/releases-lowlevel");
        httpClient.close();
        embeddedServer.close();
        github.close();
    }

    private static void assertReleases(BlockingHttpClient client, String path) {
        HttpRequest<Object> request = HttpRequest.GET(path);

        HttpResponse<List<GithubRelease>> rsp = client.exchange(request, // <4>
                Argument.listOf(GithubRelease.class)); // <5>

        assertEquals(HttpStatus.OK, rsp.getStatus());   // <6>
        assertReleases(rsp.body()); // <7>
    }

    private static void assertReleases(List<GithubRelease> releases) {
        assertNotNull(releases);
        assertTrue(releases.stream()
                .map(GithubRelease::name)
                .allMatch(name -> MICRONAUT_RELEASE.matcher(name)
                        .find()));
    }

    @Requires(property = "spec.name", value = "GithubControllerTest") // <1>
    @Controller
    static class GithubReleases {
        private final ResourceLoader resourceLoader;
        GithubReleases(ResourceLoader resourceLoader) {
            this.resourceLoader = resourceLoader;
        }

        @Produces("application/vnd.github.v3+json")
        @Get("/repos/micronaut-projects/micronaut-core/releases")
        Optional<String> coreReleases() {
            return resourceLoader.getResourceAsStream("releases.json") // <3>
                    .flatMap(inputStream -> {
                        try {
                            return Optional.of(new String(inputStream.readAllBytes(), UTF_8));
                        } catch (IOException e) {
                            return Optional.empty();
                        }
                    });
        }
    }
}
