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

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest // <1>
public class HelloControllerTest {

    @Test
    public void testHelloFilterLogging(@Client("/") HttpClient httpClient) { // <2>
        MemoryAppender appender = new MemoryAppender();
        Logger l = (Logger) LoggerFactory.getLogger(LoggingHeadersFilter.class);
        l.addAppender(appender);
        appender.start();
        BlockingHttpClient client = httpClient.toBlocking();
        assertDoesNotThrow(() -> client.retrieve(HttpRequest.GET("/")
                .header(HttpHeaders.AUTHORIZATION, "Bearer x")
                .header("foo", "bar")));
        assertTrue(appender.getEvents()
                .stream()
                .map(ILoggingEvent::getFormattedMessage)
                .anyMatch(it -> it.equals("GET / H foo:bar")));
        assertTrue(appender.getEvents()
                .stream()
                .map(ILoggingEvent::getFormattedMessage)
                .noneMatch(it -> it.equals("GET / H Authorization:Bearer x")));
        appender.stop();
    }

}