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
package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Requires
import io.micronaut.core.io.ResourceLoader
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer
import java.util.regex.Pattern
import spock.lang.Specification

class GithubControllerSpec extends Specification {
    private static Pattern MICRONAUT_RELEASE =
            Pattern.compile("Micronaut (Core |Framework )?v?\\d+.\\d+.\\d+( (RC|M)\\d)?")

    void "verify GithubReleases can Be fetched With Low Level Http Client"(String path) {
        given:
        EmbeddedServer github = ApplicationContext.run(EmbeddedServer,
                Map.of("micronaut.codec.json.additional-types", "application/vnd.github.v3+json",
                        "spec.name", "GithubControllerTest")) // <1>
        String url = "http://localhost:${github.port}"
        EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer,
                Collections.singletonMap("micronaut.http.services.github.url", url)) // <2>

        when:
        GithubConfiguration configuration = embeddedServer.applicationContext.getBean(GithubConfiguration)

        then:
        configuration.organization
        configuration.repo

        when:
        HttpClient httpClient = embeddedServer.getApplicationContext()
                .createBean(HttpClient, embeddedServer.getURL())
        BlockingHttpClient client = httpClient.toBlocking()
        HttpRequest<?> request = HttpRequest.GET(path)
        HttpResponse<List<GithubRelease>> rsp = client.exchange(request, // <4>
                Argument.listOf(GithubRelease)) // <5>

        then:
        HttpStatus.OK == rsp.getStatus()   // <6>

        when:
        List<GithubRelease> releases = rsp.body() // <7>

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

        where:
        path << ['/github/releases', '/github/releases-lowlevel']
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
            resourceLoader.getResourceAsStream("releases.json") // <3>
                    .map(inputStream -> inputStream.text)
        }
    }
}
