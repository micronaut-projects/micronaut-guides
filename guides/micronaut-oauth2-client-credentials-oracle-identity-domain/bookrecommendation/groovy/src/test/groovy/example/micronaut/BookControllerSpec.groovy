/*
 * Copyright 2017-2025 original authors
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

import io.micronaut.context.annotation.Property
import io.micronaut.core.util.StringUtils
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.StreamingHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import reactor.core.publisher.Flux
import spock.lang.IgnoreIf
import spock.lang.Specification

@Property(name = 'micronaut.security.enabled', value = StringUtils.FALSE)
@MicronautTest
class BookControllerSpec extends Specification {

    @Inject
    @Client("/")
    StreamingHttpClient client

    @IgnoreIf({ env['CI'] as boolean })
    void "retrieve books"() {
        when:
        List<BookRecommendation> books = Flux
                .from(client.jsonStream(HttpRequest.GET("/books"), BookRecommendation))
                .collectList()
                .block()

        then:
        books.size() == 1
        books[0].name == "Building Microservices"
    }
}
