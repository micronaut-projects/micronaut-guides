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

import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest // <1>
class HomeControllerTest {

    @Inject
    @Client("/")
    HttpClient httpClient; // <2>

    @Test
    void renderServerSideHTMLwithThymeleafAndMicronautViews() {
        String expected = "<!DOCTYPE html>\n" +
                "<html>\n"+
                "<head>\n"+
                "    <title>Home</title>\n"+
                "</head>\n"+
                "<body>\n"+
                "    <h1>Welcome to Micronaut for Spring</h1>\n"+
                "</body>\n"+
                "</html>";
        String html = httpClient.toBlocking().retrieve("/");
        assertEquals(expected, html);
    }
}
