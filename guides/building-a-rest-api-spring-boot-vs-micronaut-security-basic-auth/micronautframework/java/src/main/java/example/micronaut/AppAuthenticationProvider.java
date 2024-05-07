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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.provider.HttpRequestAuthenticationProvider;
import jakarta.inject.Singleton;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Singleton // <1>
class AppAuthenticationProvider<B> implements HttpRequestAuthenticationProvider<B> {

    private static final String KEY_PASSWORD = "password";
    private final Map<String, Authentication> users;
    private final PasswordEncoder passwordEncoder;

    AppAuthenticationProvider() {
        passwordEncoder = new BCryptPasswordEncoder();
        users = Map.of("sarah1",
                Authentication.build("sarah1",
                        Collections.singletonList("SAAS_SUBSCRIPTION_OWNER"),
                        Collections.singletonMap(KEY_PASSWORD, passwordEncoder.encode("abc123"))),
                "john-owns-no-subscriptions",
                Authentication.build("john-owns-no-subscriptions",
                        Collections.singletonList("NON-OWNER"),
                        Collections.singletonMap(KEY_PASSWORD, passwordEncoder.encode("qrs456"))));
    }

    @Override
    public @NonNull AuthenticationResponse authenticate(@Nullable HttpRequest<B> request,
                                                        @NonNull AuthenticationRequest<String, String> form) {
        return Optional.ofNullable(users.get(form.getIdentity()))
                .filter(a -> passwordEncoder.matches(form.getSecret(), a.getAttributes().get(KEY_PASSWORD).toString()))
                .map(authentication -> AuthenticationResponse.success(form.getIdentity(), authentication.getRoles()))
                .orElse(AuthenticationResponse.failure());
    }
}
