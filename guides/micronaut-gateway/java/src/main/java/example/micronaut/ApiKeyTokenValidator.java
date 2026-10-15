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

import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.token.validator.TokenValidator;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import java.util.Collections;

@Singleton
class ApiKeyTokenValidator implements TokenValidator<HttpRequest<?>> {
    @Override
    public Publisher<Authentication> validateToken(String token, @Nullable HttpRequest<?> request) {
        if (token != null && token.equals("XXX")) {
            return Publishers.just(Authentication.build("sherlock", Collections.singleton("ROLE_DETECTIVE")));
        } else if (token != null && token.equals("YYY")) {
            return Publishers.just(Authentication.build("watson", Collections.singleton("ROLE_USER")));
        }
        return Publishers.empty();
    }
}
