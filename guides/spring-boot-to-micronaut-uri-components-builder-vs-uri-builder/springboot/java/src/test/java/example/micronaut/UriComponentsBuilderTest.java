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

import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UriComponentsBuilderTest {

    @Test
    void youCanUseUriComponentsBuilderToBuildUris() {
        String isbn = "1680502395";
        assertEquals("/book/1680502395?lang=es", UriComponentsBuilder.fromUriString("/book")
                .path("/" + isbn)
                .queryParam("lang", "es")
                .build()
                .toUriString());
    }
}
