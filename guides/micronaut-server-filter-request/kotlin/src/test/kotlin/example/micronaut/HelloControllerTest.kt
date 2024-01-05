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

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

@MicronautTest
class HelloControllerTest {

    @Test
    fun testHelloFilterLogging(@Client("/") httpClient: HttpClient) { // <2>
        val appender = MemoryAppender()
        val l = LoggerFactory.getLogger(LoggingHeadersFilter::class.java) as Logger
        l.addAppender(appender)
        appender.start()

        val client = httpClient.toBlocking()
        assertDoesNotThrow<String> {
            client.retrieve(
                HttpRequest.GET<Any>("/")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer x")
                    .header("foo", "bar")
            )
        }
        assertTrue(appender.getEvents()
            .stream()
            .map { obj: ILoggingEvent -> obj.formattedMessage }
            .anyMatch { it == "GET / H foo:bar" }
        )
        assertTrue(appender.getEvents()
            .stream()
            .map { obj: ILoggingEvent -> obj.formattedMessage }
            .noneMatch { it.contains("Authorization", true) }
        )
        appender.stop()
    }
}