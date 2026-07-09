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

import io.micronaut.context.ApplicationContext
import io.micronaut.core.type.Argument
import io.micronaut.core.util.StringUtils
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.Specification

class InfoEndpointSpec extends Specification {

    void 'crac information is exposed in the info endpoint'() {
        given:
        EmbeddedServer server = ApplicationContext.run(EmbeddedServer, [
            'endpoints.info.enabled'  : StringUtils.TRUE,
            'endpoints.info.sensitive': StringUtils.FALSE
        ])
        HttpClient client = server.applicationContext.createBean(HttpClient, server.URL)

        when:
        Map<String, Map<String, Integer>> json = client.toBlocking().retrieve(
            HttpRequest.GET('/info'),
            Argument.mapOf(Argument.of(String), Argument.mapOf(String, Integer))
        )

        then:
        json == ['crac': ['restore-time': -1, 'uptime-since-restore': -1]]

        cleanup:
        client.close()
        server.close()
    }
}
