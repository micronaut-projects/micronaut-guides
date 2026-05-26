/*
 * Copyright 2017-2026 original authors
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
package example.micronaut.model

import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Property(name = 'spec.name', value = 'BookAvailabilitySpec') // <6>
@MicronautTest
class BookAvailabilitySpec extends Specification {
    @Inject
    @Client('/')
    HttpClient httpClient

    void 'from value maps OpenAPI enum values'() { // <1>
        expect:
        BookAvailability.fromValue('available') == BookAvailability.AVAILABLE
        BookAvailability.fromValue('not available') == BookAvailability.NOT_AVAILABLE
        BookAvailability.fromValue('reserved') == BookAvailability.RESERVED
    }

    void 'to string renders OpenAPI enum values'() { // <2>
        expect:
        BookAvailability.AVAILABLE.toString() == 'available'
        BookAvailability.NOT_AVAILABLE.toString() == 'not available'
        BookAvailability.RESERVED.toString() == 'reserved'
    }

    void 'json creator binds query value'() { // <7>
        given:
        HttpRequest<?> request = HttpRequest.GET(UriBuilder.of('/bookavailability')
                .queryParam('availability', 'reserved')
                .build())

        expect:
        httpClient.toBlocking().retrieve(request) == 'reserved'
    }

    @Requires(property = 'spec.name', value = 'BookAvailabilitySpec') // <5>
    @Controller('/bookavailability') // <3>
    static class BookAvailabilityController {
        @Secured(SecurityRule.IS_ANONYMOUS)
        @Produces(MediaType.TEXT_PLAIN)
        @Get // <4>
        String index(@QueryValue BookAvailability availability) {
            availability.toString()
        }
    }
}
