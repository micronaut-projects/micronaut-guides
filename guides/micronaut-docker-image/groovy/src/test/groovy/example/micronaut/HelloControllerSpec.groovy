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
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification
import io.micronaut.context.BeanContext

@MicronautTest
class HelloControllerSpec extends Specification {

    @Inject
    BeanContext beanContext

    void "apex returns JSON"() {
        given:
        HttpClient httpClient = createHttpClient(beanContext)

        when:
        String body = httpClient.toBlocking().retrieve(HttpRequest.GET("/"))
        then:
        body
        "{\"message\":\"Hello World\"}" == body
    }

    private static HttpClient createHttpClient(BeanContext beanContext) {
        beanContext.createBean(HttpClient, "http://localhost:" + beanContext.getBean(EmbeddedServer).getPort())
    }
}