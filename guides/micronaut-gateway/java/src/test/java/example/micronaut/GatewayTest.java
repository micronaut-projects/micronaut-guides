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
import io.micronaut.core.type.Argument;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.exceptions.HttpClientException;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.security.filters.SecurityFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class GatewayTest {

    @Test
    void gateway() {
        EmbeddedServer grailsServer = ApplicationContext.run(EmbeddedServer.class, Map.of("micronaut.security.filter.enabled", StringUtils.FALSE, "framework", "grails"));
        HttpClient grailsHttpClient = grailsServer.getApplicationContext().createBean(HttpClient.class, grailsServer.getURL());
        BlockingHttpClient grailsClient = grailsHttpClient.toBlocking();

        EmbeddedServer micronautServer = ApplicationContext.run(EmbeddedServer.class, Map.of("micronaut.security.filter.enabled", StringUtils.FALSE,"framework", "micronaut"));
        HttpClient micronautHttpClient = micronautServer.getApplicationContext().createBean(HttpClient.class, micronautServer.getURL());
        BlockingHttpClient micronautClient = micronautHttpClient.toBlocking();

        Map<String, Object> configuration = Map.of(
                "micronaut.gateway.routes.micronaut.uri", micronautServer.getURL().toString(),
                "micronaut.gateway.routes.micronaut.roles-allowed[0]", "ROLE_DETECTIVE",
                "micronaut.gateway.routes.micronaut.predicates[0].path", "/micronaut/books",
                "micronaut.gateway.routes.grails.uri", grailsServer.getURL().toString(),
                "micronaut.gateway.routes.grails.predicates[0].path", "/grails/books",
                "micronaut.gateway.routes.grails.roles-allowed[0]", "ROLE_DETECTIVE",
                "micronaut.gateway.routes.grails.roles-allowed[1]", "ROLE_USER"
        );
        EmbeddedServer server = ApplicationContext.run(EmbeddedServer.class, configuration);
        assertTrue(server.getApplicationContext().containsBean(SecurityFilter.class));
        HttpClient httpClient = server.getApplicationContext().createBean(HttpClient.class, server.getURL());
        BlockingHttpClient client = httpClient.toBlocking();

        String sherlockApiKey = "XXX";
        HttpRequest<?> micronautRequest = HttpRequest.GET("/micronaut/books").bearerAuth(sherlockApiKey);
        HttpRequest<?> grailsRequest = HttpRequest.GET("/grails/books").bearerAuth(sherlockApiKey);
        List<Book> books = assertDoesNotThrow(() -> grailsClient.retrieve(grailsRequest, Argument.listOf(Book.class)));
        assertEquals(6, books.size());

        books = assertDoesNotThrow(() -> micronautClient.retrieve(micronautRequest, Argument.listOf(Book.class)));
        assertEquals(2, books.size());

        books = assertDoesNotThrow(() -> client.retrieve(micronautRequest, Argument.listOf(Book.class)));
        assertEquals(2, books.size());

        books = assertDoesNotThrow(() -> client.retrieve(grailsRequest, Argument.listOf(Book.class)));
        assertEquals(6, books.size());

        String watsonApiKey = "YYY";
        HttpRequest<?> watsonMicronautRequest = HttpRequest.GET("/micronaut/books").bearerAuth(watsonApiKey);
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> client.retrieve(watsonMicronautRequest, Argument.listOf(Book.class)));
        assertEquals(HttpStatus.FORBIDDEN, thrown.getResponse().getStatus());

        HttpRequest<?> anonymousMicronautRequest = HttpRequest.GET("/micronaut/books");
        thrown = assertThrows(HttpClientResponseException.class, () -> client.retrieve(anonymousMicronautRequest, Argument.listOf(Book.class)));
        assertEquals(HttpStatus.UNAUTHORIZED, thrown.getResponse().getStatus());

        grailsServer.close();
        micronautServer.close();

        assertThrows(HttpClientException.class, () -> client.exchange(micronautRequest));

        assertThrows(HttpClientException.class, () -> client.exchange(grailsRequest));

        server.close();
    }
}
