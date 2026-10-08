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
package example.micronaut

import io.micronaut.context.annotation.Requires
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.security.authentication.AuthenticationFailureReason
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import io.micronaut.security.authentication.provider.HttpRequestAuthenticationProvider
import io.micronaut.test.annotation.Sql
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import jakarta.inject.Singleton
import spock.lang.Specification

@Sql('classpath:employees-test-data.sql')
@MicronautTest // <1>
class HomeControllerSpec extends Specification implements TestPropertyProvider {

    private static final String USERNAME = 'emma@supremo.onmicrosoft.com'
    private static final String PASSWORD = 'secret'

    @Inject
    @Client('/')
    HttpClient httpClient

    @Override
    @NonNull
    Map<String, String> getProperties() {
        Oracle.getProperties() + ['spec.name': 'HomeControllerSpec', 'micronaut.server.port': '-1'] // <2>
    }

    void 'anonymous users are invited to sign in'() {
        given:
        BlockingHttpClient client = httpClient.toBlocking()

        when:
        String html = client.retrieve(HttpRequest.GET('/').accept(MediaType.TEXT_HTML))

        then:
        html.contains('Anonymous')
        html.contains('Sign in with Microsoft Entra ID')
        html.contains('href="/oauth/login/entraid"') // <3>
    }

    void 'signed in users see the employees and the salary summary'() {
        given:
        BlockingHttpClient client = httpClient.toBlocking()

        when:
        String html = client.retrieve(HttpRequest.GET('/').accept(MediaType.TEXT_HTML).basicAuth(USERNAME, PASSWORD)) // <4>

        then:
        html.contains("Signed in as <span class=\"fw-semibold\">${USERNAME}</span>")
        html.contains('href="/logout"')
        html.contains('9826.00') // <5>
        ['Victoria', 'Marvin', 'Chris', 'Emma', 'Taylor'].every { html.contains(it) } // <6>
    }

    @Requires(property = 'spec.name', value = 'HomeControllerSpec') // <7>
    @Singleton
    static class BasicAuthProvider<B> implements HttpRequestAuthenticationProvider<B> { // <8>

        @Override
        @NonNull
        AuthenticationResponse authenticate(@Nullable HttpRequest<B> requestContext,
                                            @NonNull AuthenticationRequest<String, String> authRequest) {
            if (USERNAME == authRequest.identity && PASSWORD == authRequest.secret) {
                return AuthenticationResponse.success(USERNAME, [preferred_username: USERNAME] as Map<String, Object>) // <9>
            }
            AuthenticationResponse.failure(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH)
        }
    }
}
