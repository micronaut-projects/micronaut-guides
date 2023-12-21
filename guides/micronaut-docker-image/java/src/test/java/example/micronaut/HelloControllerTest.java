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

import io.micronaut.context.BeanContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest // <1>
class HelloControllerTest {
    @Inject
    BeanContext beanContext;

    @Test
    void testHello() {
        HttpClient client = createHttpClient(beanContext);
        String body = client.toBlocking().retrieve(HttpRequest.GET("/"));
        assertNotNull(body);
        assertEquals("{\"message\":\"Hello World\"}", body);
    }

    private static HttpClient createHttpClient(BeanContext beanContext) {
        return beanContext.createBean(HttpClient.class, "http://localhost:" + beanContext.getBean(EmbeddedServer.class).getPort());
    }
}