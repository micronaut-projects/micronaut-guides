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
import org.junit.jupiter.api.Test;
import io.micronaut.context.annotation.Property;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest // <1>
@Property(name = "vat.country", value = "Switzerland") // <2>
@Property(name = "vat.rate", value = "7.7") // <2>
class VatControllerTest {

    @Test
    void vatExposesTheValueAddedTaxRate(@Client("/") HttpClient httpClient) {
        assertEquals("{\"rate\":7.7}", httpClient.toBlocking().retrieve("/vat"));
    }
}
