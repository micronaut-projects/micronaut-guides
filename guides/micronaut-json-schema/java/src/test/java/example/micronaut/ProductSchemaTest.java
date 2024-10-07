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
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@MicronautTest
class ProductSchemaTest {

    @Test
    void testProductSchema(@Client("/")HttpClient httpClient) throws JSONException {
        BlockingHttpClient client = httpClient.toBlocking();
        URI uri = UriBuilder.of("/schemas").path("product.schema.json").build();
        HttpRequest<?> request = HttpRequest.GET(uri);
        String json = assertDoesNotThrow(() -> client.retrieve(request));
        assertNotNull(json);
        String expected = """
{
  "$schema": "https://json-schema.org/draft/2020-12/schema",
  "$id": "http://localhost:8080/schemas/product.schema.json",
  "title": "Product",
  "description": "A product from Acme's catalog",
  "type": ["object"],
  "properties": {
    "productId": {
      "description": "The unique identifier for a product",
      "type": ["integer"]
    },
    "productName": {
      "description": "Name of the product",
      "type": ["string"]
    },
    "price": {
      "description": "The price of the product",
      "type": ["number"],
      "exclusiveMinimum": 0
    },
    "tags": {
      "type": ["array"],
      "items": {
        "type": ["string"]
      },
      "minItems": 1,
      "uniqueItems": true
    }
  },
  "required": [ "productId", "productName", "price" ]
}""";
        JSONAssert.assertEquals(expected, json, JSONCompareMode.LENIENT);
    }
}
