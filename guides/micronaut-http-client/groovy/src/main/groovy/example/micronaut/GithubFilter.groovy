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

import io.micronaut.context.annotation.Requires
import io.micronaut.http.MutableHttpRequest
import io.micronaut.http.annotation.ClientFilter
import io.micronaut.http.annotation.RequestFilter

@ClientFilter("/repos/**") // <1>
@Requires(property = GithubConfiguration.PREFIX + ".username") // <2>
@Requires(property = GithubConfiguration.PREFIX + ".token") // <2>
class GithubFilter {

    private final GithubConfiguration configuration

    GithubFilter(GithubConfiguration configuration) { // <3>
        this.configuration = configuration
    }

    @RequestFilter // <4>
    void doFilter(MutableHttpRequest<?> request) {
        request.basicAuth(configuration.username, configuration.token) // <5>
    }
}
