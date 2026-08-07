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

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import io.micronaut.security.authentication.provider.HttpRequestAuthenticationProvider
import jakarta.inject.Singleton
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@CompileStatic
@Singleton // <1>
class AppAuthenticationProvider<B> implements HttpRequestAuthenticationProvider<B> {

    private static final String KEY_PASSWORD = "password"
    private final PasswordEncoder passwordEncoder
    private final Map<String, Authentication> users

    AppAuthenticationProvider() {
        passwordEncoder = new BCryptPasswordEncoder()
        users = [
                "sarah1": Authentication.build("sarah1",
                        ["SAAS_SUBSCRIPTION_OWNER"],
                        [(KEY_PASSWORD): passwordEncoder.encode("abc123")]),
                "john-owns-no-subscriptions": Authentication.build("john-owns-no-subscriptions",
                        ["NON-OWNER"],
                        [(KEY_PASSWORD): passwordEncoder.encode("qrs456")])
        ]
    }

    @Override
    @NonNull
    AuthenticationResponse authenticate(@Nullable HttpRequest<B> request,
                                        @NonNull AuthenticationRequest<String, String> form) {
        Authentication authentication = users[form.identity]
        if (authentication && passwordEncoder.matches(form.secret, authentication.attributes[KEY_PASSWORD].toString())) {
            return AuthenticationResponse.success(form.identity, authentication.roles)
        }
        AuthenticationResponse.failure()
    }
}
