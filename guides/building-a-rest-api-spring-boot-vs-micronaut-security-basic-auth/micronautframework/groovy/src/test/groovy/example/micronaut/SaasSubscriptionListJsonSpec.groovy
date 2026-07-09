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

import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import io.micronaut.core.io.ResourceLoader
import io.micronaut.json.JsonMapper
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false) // <1>
class SaasSubscriptionListJsonSpec extends Specification {

    @Inject
    JsonMapper json

    @Inject
    ResourceLoader resourceLoader

    void "SaaS subscription serialization test"() { // <2>
        given:
        SaasSubscription subscription = new SaasSubscription(99L, "Professional", 4900, "sarah1")
        String expected = getResourceAsString("expected.json")

        when:
        String result = json.writeValueAsString(subscription)
        DocumentContext documentContext = JsonPath.parse(result)
        Number id = documentContext.read('$.id')
        String name = documentContext.read('$.name')
        Number cents = documentContext.read('$.cents')

        then:
        result.replaceAll("\\s", "") == expected.replaceAll("\\s", "")
        id != null
        id == 99
        name != null
        name == "Professional"
        cents != null
        cents == 4900
    }

    void "SaaS subscription deserialization test"() { // <2>
        given:
        String expected = """
           {
               "id":100,
               "name": "Advanced",
               "cents":2900,
               "owner": "sarah1"
           }
           """

        expect:
        json.readValue(expected, SaasSubscription) == new SaasSubscription(100L, "Advanced", 2900, "sarah1")
        json.readValue(expected, SaasSubscription).id == 100
        json.readValue(expected, SaasSubscription).name == "Advanced"
        json.readValue(expected, SaasSubscription).cents == 2900
    }

    private String getResourceAsString(String resourceName) {
        resourceLoader.getResourceAsStream(resourceName)
                .map { stream -> stream.withCloseable { it.getText("UTF-8") } }
                .orElse("")
    }
}
