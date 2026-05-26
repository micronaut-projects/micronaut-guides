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

import com.jayway.jsonpath.JsonPath
import io.micronaut.core.io.ResourceLoader
import io.micronaut.json.JsonMapper
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.nio.charset.StandardCharsets

@MicronautTest(startApplication = false) // <1>
class SaasSubscriptionJsonTest {

    @Test
    fun saasSubscriptionSerializationTest(json: JsonMapper, resourceLoader: ResourceLoader) { // <2>
        val subscription = SaasSubscription(99L, "Professional", 4900)
        val expected = getResourceAsString(resourceLoader, "expected.json")
        val result = json.writeValueAsString(subscription)
        assertThat(result).isEqualToIgnoringWhitespace(expected)
        val documentContext = JsonPath.parse(result)
        val id = documentContext.read<Number>("$.id")
        assertThat(id)
            .isNotNull()
            .isEqualTo(99)

        val name = documentContext.read<String>("$.name")
        assertThat(name)
            .isNotNull()
            .isEqualTo("Professional")

        val cents = documentContext.read<Number>("$.cents")
        assertThat(cents)
            .isNotNull()
            .isEqualTo(4900)
    }

    @Test
    fun saasSubscriptionDeserializationTest(json: JsonMapper) { // <2>
        val expected = """
           {
               "id":100,
               "name": "Advanced",
               "cents":2900
           }
           """.trimIndent()
        assertThat(json.readValue(expected, SaasSubscription::class.java))
            .isEqualTo(SaasSubscription(100L, "Advanced", 2900))
        val subscription = json.readValue(expected, SaasSubscription::class.java)!!
        assertThat(subscription.id).isEqualTo(100L)
        assertThat(subscription.name).isEqualTo("Advanced")
        assertThat(subscription.cents).isEqualTo(2900)
    }

    private fun getResourceAsString(resourceLoader: ResourceLoader, resourceName: String): String =
        resourceLoader.getResourceAsStream(resourceName)
            .map { stream ->
                stream.bufferedReader(StandardCharsets.UTF_8).use { it.readText() }
            }
            .orElseThrow { IllegalStateException("Resource $resourceName not found") }
}
