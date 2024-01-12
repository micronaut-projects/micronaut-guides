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

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.NonNull
import io.micronaut.http.HttpRequest
import jakarta.inject.Singleton

@CompileStatic
@Singleton // <1>
class HttpHeaderColorFetcher implements ColorFetcher {

    @Override
    @NonNull
    Optional<String> favouriteColor(@NonNull HttpRequest<?> request) {
        return request.headers.get('color', String)
    }

    @Override
    int getOrder() { // <2>
        return 10
    }
}
