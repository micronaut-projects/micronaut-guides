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

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Shared
import spock.lang.Specification
import jakarta.inject.Inject

@MicronautTest // <1>
class InfoSpec extends Specification {

    @Shared
    @Client("/")
    @Inject
    HttpClient client // <2>

    void 'test git commit info appears in JSON'() {
        given:
        HttpRequest request = HttpRequest.GET('/info') // <3>

        when:
        HttpResponse<Map> rsp = client.toBlocking().exchange(request, Map)

        then:
        rsp.status().code == 200

        when:
        Map json = rsp.body() // <4>

        then:
        json.git
        json.git.commit
        json.git.commit.message
        json.git.commit.time
        json.git.commit.id
        json.git.commit.user
        json.git.branch
    }
}