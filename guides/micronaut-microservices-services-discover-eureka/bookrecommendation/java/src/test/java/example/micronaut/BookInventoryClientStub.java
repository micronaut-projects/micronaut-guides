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
package example.micronaut;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.retry.annotation.Fallback;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

import jakarta.validation.constraints.NotBlank;

import static io.micronaut.context.env.Environment.TEST;

@Requires(env = TEST) // <1>
@Fallback
@Singleton
public class BookInventoryClientStub implements BookInventoryOperations {

    @Override
    public Mono<Boolean> stock(@NonNull @NotBlank String isbn) {
        if (isbn.equals("1491950358")) {
            return Mono.just(true); // <2>
        }
        if (isbn.equals("1680502395")) {
            return Mono.just(false); // <3>
        }
        return Mono.empty(); // <4>
    }
}
