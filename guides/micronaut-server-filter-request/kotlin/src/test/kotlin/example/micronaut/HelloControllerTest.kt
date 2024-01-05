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
package example.micronaut

import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@MicronautTest // <1>
class HelloControllerTest {

    @Test
    fun testHello(
        @Client("/") httpClient: HttpClient,  // <2>
        filter: LoggingHeadersFilterOverride
    ) {
        val client = httpClient.toBlocking()
        Assertions.assertDoesNotThrow<String> {
            client.retrieve(
                HttpRequest.GET<Any>("/")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer x")
                    .header("foo", "bar")
            )
        }
        Assertions.assertFalse(filter.headers.containsKey(HttpHeaders.AUTHORIZATION))
        Assertions.assertTrue(filter.headers.containsKey("foo"))
        filter.clear()
    }
}