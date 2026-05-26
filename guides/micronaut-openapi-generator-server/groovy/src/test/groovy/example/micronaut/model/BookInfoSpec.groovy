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

//tag::imports[]
import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.validation.Validator
import spock.lang.Specification
//end::imports[]

/**
 * Model tests for BookInfo
 */
//tag::annotations[]
@Property(name = 'spec.name', value = 'BookInfoSpec') // <2>
@MicronautTest
class BookInfoSpec extends Specification {
    //end::annotations[]
    //tag::requiredProperties[]
    @Inject
    Validator validator // <1>

    void 'name is required'() {
        expect:
        validator.validate(new BookInfo('Alice\'s Adventures in Wonderland', BookAvailability.AVAILABLE)).empty // <2>
        !validator.validate(new BookInfo(null, BookAvailability.AVAILABLE)).empty // <3>
    }

    void 'availability is required'() { // <4>
        expect:
        validator.validate(new BookInfo('Alice\'s Adventures in Wonderland', BookAvailability.RESERVED)).empty
        !validator.validate(new BookInfo('Alice\'s Adventures in Wonderland', null)).empty
    }
    //end::requiredProperties[]

    //tag::otherProperties[]
    void 'author is optional but must have enough characters when present'() {
        expect:
        validator.validate(new BookInfo('Alice\'s Adventures in Wonderland', BookAvailability.AVAILABLE)
                .author(null)).empty

        validator.validate(new BookInfo('Alice\'s Adventures in Wonderland', BookAvailability.AVAILABLE)
                .author('Lewis Carroll')).empty // <1>

        !validator.validate(new BookInfo('Alice\'s Adventures in Wonderland', BookAvailability.AVAILABLE)
                .author('fo')).empty // <2>
    }

    void 'isbn is optional but must match the pattern when present'() {
        expect:
        validator.validate(new BookInfo('Alice\'s Adventures in Wonderland', BookAvailability.AVAILABLE)
                .isbn(null)).empty

        validator.validate(new BookInfo('Alice\'s Adventures in Wonderland', BookAvailability.AVAILABLE)
                .isbn('9783161484100')).empty // <3>

        !validator.validate(new BookInfo('Alice\'s Adventures in Wonderland', BookAvailability.AVAILABLE)
                .isbn('9783161 84100')).empty // <4>
    }
    //end::otherProperties[]

    //tag::jsonSerialization[]
    @Inject
    @Client('/')
    HttpClient httpClient

    void 'book info serializes to and from json'() {
        given:
        BookInfo requiredBookInfo = new BookInfo('Alice\'s Adventures in Wonderland', BookAvailability.AVAILABLE)
                .author('Lewis Carroll')
                .isbn('9783161484100')

        when:
        BookInfo bookInfo = httpClient.toBlocking().retrieve(HttpRequest.GET('/bookinfo'), BookInfo) // <5>

        then:
        bookInfo == requiredBookInfo
    }

    @Requires(property = 'spec.name', value = 'BookInfoSpec') // <3>
    @Controller('/bookinfo') // <1>
    static class BookInfoSerdeController {
        @Secured(SecurityRule.IS_ANONYMOUS)
        @Get
        BookInfo index() { // <4>
            new BookInfo('Alice\'s Adventures in Wonderland', BookAvailability.AVAILABLE)
                    .author('Lewis Carroll')
                    .isbn('9783161484100')
        }
    }
    //end::jsonSerialization[]
}
