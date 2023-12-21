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
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

import static io.micronaut.http.HttpStatus.BAD_REQUEST

@Property(name = 'spec.name', value = 'FruitControllerValidationSpec')
@MicronautTest
class FruitControllerValidationSpec extends Specification {

    @Inject
    @Client('/')
    HttpClient httpClient

    void testFruitIsValidated() {
        when:
        httpClient.toBlocking().exchange(HttpRequest.POST('/fruits', new Fruit('', 'Hola')))

        then:
        HttpClientResponseException e = thrown()
        BAD_REQUEST == e.status
    }
}
