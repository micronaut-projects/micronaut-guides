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

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.security.endpoints.TokenRefreshRequest
import io.micronaut.security.token.render.BearerAccessRefreshToken
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

import static io.micronaut.http.HttpStatus.BAD_REQUEST

@MicronautTest
class UnsignedRefreshTokenSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient client

    void 'Accessing a secured URL without authenticating returns unauthorized'() {

        given:
        String unsignedRefreshedToken = "foo" // <1>

        when:
        Argument<BearerAccessRefreshToken> bodyArgument = Argument.of(BearerAccessRefreshToken)
        Argument<Map> errorArgument = Argument.of(Map)

        client.toBlocking().exchange(
                HttpRequest.POST("/oauth/access_token", new TokenRefreshRequest(TokenRefreshRequest.GRANT_TYPE_REFRESH_TOKEN, unsignedRefreshedToken)),
                bodyArgument,
                errorArgument)

        then:
        HttpClientResponseException e = thrown()
        e.status == BAD_REQUEST

        when:
        Optional<Map> mapOptional = e.response.getBody(Map)

        then:
        mapOptional.isPresent()

        when:
        Map m = mapOptional.get()

        then:
        m.error == 'invalid_grant'
        m.error_description == 'Refresh token is invalid'
    }
}
