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
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest // <1>
class PhotosControllerTest {

    @Test
    void photoTest(@Client("/") HttpClient httpClient) { // <2>
        BlockingHttpClient client = httpClient.toBlocking();
        URI uri  = UriBuilder.of("/photos").path("1").build();
        HttpRequest<?> request = HttpRequest.GET(uri).accept(MediaType.TEXT_HTML);
        String html = assertDoesNotThrow(() -> client.retrieve(request));
        assertNotNull(html);
        assertTrue(html.contains("<!DOCTYPE html>"));
        String expectedUrl = "https://via.placeholder.com/600/92c952";
        String expectedTitle = "accusamus beatae ad facilis cum similique qui sunt";
        String expectedThumbnailUrl = "https://via.placeholder.com/150/92c952";
        assertTrue(html.contains("<h1>" + expectedTitle + "</h1>"));
        assertTrue(html.contains("<a href=\"" + expectedUrl + "\"><img src=\"" + expectedThumbnailUrl + "\" alt=\"" + expectedTitle + "\"/></a>"));
    }
}
