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

import com.nimbusds.jwt.JWTParser
import com.nimbusds.jwt.SignedJWT
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.render.BearerAccessRefreshToken
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

import static io.micronaut.http.HttpStatus.OK
import static io.micronaut.http.HttpStatus.UNAUTHORIZED
import static io.micronaut.http.MediaType.TEXT_PLAIN

@MicronautTest // <1>
class JwtAuthenticationSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient client // <2>

    void 'Accessing a secured URL without authenticating returns unauthorized'() {
        when:
        client.toBlocking().exchange(HttpRequest.GET('/').accept(TEXT_PLAIN))

        then:
        HttpClientResponseException e = thrown()
        e.status == UNAUTHORIZED // <3>
    }

    void "upon successful authentication, a JSON Web token is issued to the user"() {
        when: 'Login endpoint is called with valid credentials'
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials("sherlock", "password")
        HttpRequest request = HttpRequest.POST('/login', creds) // <4>
        HttpResponse<BearerAccessRefreshToken> rsp = client.toBlocking().exchange(request, BearerAccessRefreshToken) // <5>

        then: 'the endpoint can be accessed'
        rsp.status == OK

        when:
        BearerAccessRefreshToken bearerAccessRefreshToken = rsp.body()

        then:
        bearerAccessRefreshToken.username == 'sherlock'
        bearerAccessRefreshToken.accessToken

        and: 'the access token is a signed JWT'
        JWTParser.parse(bearerAccessRefreshToken.accessToken) instanceof SignedJWT

        when: 'passing the access token as in the Authorization HTTP Header with the prefix Bearer allows the user to access a secured endpoint'
        String accessToken = bearerAccessRefreshToken.accessToken
        HttpRequest requestWithAuthorization = HttpRequest.GET('/' )
                .accept(TEXT_PLAIN)
                .bearerAuth(accessToken) // <6>
        HttpResponse<String> response = client.toBlocking().exchange(requestWithAuthorization, String)

        then:
        response.status == OK
        response.body() == 'sherlock' // <7>
    }
}
