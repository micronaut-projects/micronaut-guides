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

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import jakarta.inject.Inject;
import java.time.Month;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class NewsControllerTest {

    @Inject
    EmbeddedServer server;

    @Inject
    @Client("/")
    HttpClient client;

    @Timeout(5) // <1>
    @Test
    void fetchingOctoberHeadlinesUsesCache() {
        HttpRequest request = HttpRequest.GET(UriBuilder.of("/").path(Month.OCTOBER.name()).build());
        News news = client.toBlocking().retrieve(request, News.class);
        String expected = "Micronaut AOP: Awesome flexibility without the complexity";
        assertEquals(Arrays.asList(expected), news.getHeadlines());

        news = client.toBlocking().retrieve(request, News.class);
        assertEquals(Arrays.asList(expected), news.getHeadlines());
    }
}
