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
/*
//tag::pkg[]
package example.micronaut;
//end::pkg[]
*/
//tag::imports[]

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest // <1>
class RequestScopeTest {
    @Inject
    @Client("/")
    HttpClient httpClient; // <2>
    // end::imports[]
// tag::test[]
    @Test
    void requestScopeScopeIsACustomScopeThatIndicatesANewInstanceOfTheBeanIsCreatedAndAssociatedWithEachHTTPRequest() {
        String path = "/request";
        BlockingHttpClient client = httpClient.toBlocking();
        Set<String> responses = new HashSet<>(executeRequest(client, path));
        assertEquals(1, responses.size()); // <3>
        responses.addAll(executeRequest(client, path));
        assertEquals(2, responses.size()); // <4>
    }

    private List<String> executeRequest(BlockingHttpClient client, 
                                        String path) {
        return client.retrieve(createRequest(path), Argument.listOf(String.class));
    }
    private HttpRequest<?> createRequest(String path) {
        return HttpRequest.GET(path).header("UUID", UUID.randomUUID().toString());
    }
}
//end::test[]