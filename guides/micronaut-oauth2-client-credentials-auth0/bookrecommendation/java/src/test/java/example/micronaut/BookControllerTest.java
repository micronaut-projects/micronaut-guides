/*
 * Copyright 2017-2023 original authors
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
import io.micronaut.http.client.StreamingHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import reactor.core.publisher.Flux;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import io.micronaut.core.util.StringUtils;
import io.micronaut.context.annotation.Property;

@Property(name = "micronaut.security.enabled", value= StringUtils.FALSE)
@MicronautTest
public class BookControllerTest {

    @Inject
    @Client("/")
    StreamingHttpClient client;

    @DisabledIfEnvironmentVariable(named = "CI", matches = "true")
    @Test
    public void testRetrieveBooks() {
        List<BookRecommendation> books = Flux
                .from(client.jsonStream(HttpRequest.GET("/books"), BookRecommendation.class))
                .collectList()
                .block();
        assertEquals(1, books.size());
        assertEquals("Building Microservices", books.get(0).getName());
    }
}
