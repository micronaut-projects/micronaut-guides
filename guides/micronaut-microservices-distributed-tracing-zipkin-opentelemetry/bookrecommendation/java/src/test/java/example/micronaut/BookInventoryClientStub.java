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

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.retry.annotation.Fallback;
import org.reactivestreams.Publisher;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;
import reactor.core.publisher.Mono;

@Requires(env = Environment.TEST)
@Fallback
@Singleton
public class BookInventoryClientStub implements BookInventoryOperations {

    @Override
    public Mono<Boolean> stock(@NotBlank String isbn) {
        if (isbn.equals("1491950358")) {
            return Mono.just(Boolean.TRUE);

        } else if (isbn.equals("1680502395")) {
            return Mono.just(Boolean.FALSE);
        }
        return Mono.empty();
    }
}
