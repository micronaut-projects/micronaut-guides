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
package example.micronaut

import io.micronaut.context.annotation.Property
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Property(name = "endpoints.all.path", value = "/endpoints/") // <1>
@MicronautTest
class HealthPathSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient client

    void "health endpoint exposed at non-default endpoints path"() {
        when:
        HttpStatus status = client.toBlocking().retrieve(HttpRequest.GET("/endpoints/health"), HttpStatus) // <2>

        then:
        status == HttpStatus.OK

        when:
        client.toBlocking().retrieve(HttpRequest.GET("/health"), HttpStatus) // <3>

        then:
        HttpClientResponseException thrown = thrown()
        thrown.status == HttpStatus.NOT_FOUND
    }
}
