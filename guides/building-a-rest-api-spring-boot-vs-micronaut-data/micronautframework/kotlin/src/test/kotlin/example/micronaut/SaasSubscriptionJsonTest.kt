package example.micronaut

import com.jayway.jsonpath.JsonPath
import io.micronaut.core.io.ResourceLoader
import io.micronaut.json.JsonMapper
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors

@MicronautTest(startApplication = false) // <1>
class SaasSubscriptionJsonTest {

    @Test
    @Throws(IOException::class)
    fun saasSubscriptionSerializationTest(json: JsonMapper, resourceLoader: ResourceLoader) { // <2>
        val subscription = SaasSubscription(99, "Professional", 4900)
        val expected = getResourceAsString(resourceLoader, "expected.json")
        val result = json.writeValueAsString(subscription)
        assertThat(result).isEqualToIgnoringWhitespace(expected)
        val documentContext = JsonPath.parse(result)
        val id: Number = documentContext.read("$.id")
        assertThat(id)
            .isNotNull()
            .isEqualTo(99)

        val name: String = documentContext.read("$.name")
        assertThat(name)
            .isNotNull()
            .isEqualTo("Professional")

        val cents: Number = documentContext.read("$.cents")
        assertThat(cents)
            .isNotNull()
            .isEqualTo(4900)
    }

    @Test
    @Throws(IOException::class)
    fun saasSubscriptionDeserializationTest(json: JsonMapper) { // <2>
        val expected = """
           {
               "id":100,
               "name": "Advanced",
               "cents":2900
           }
        """.trimIndent()
        val subscription = json.readValue(expected, SaasSubscription::class.java)
        assertThat(subscription)
            .isEqualTo(SaasSubscription(100, "Advanced", 2900))
        assertThat(subscription!!.id).isEqualTo(100)
        assertThat(subscription.name).isEqualTo("Advanced")
        assertThat(subscription.cents).isEqualTo(2900)
    }

    private fun getResourceAsString(resourceLoader: ResourceLoader, resourceName: String): String =
        resourceLoader.getResourceAsStream(resourceName)
            .map { stream ->
                stream.use {
                    BufferedReader(InputStreamReader(it, StandardCharsets.UTF_8)).use { reader ->
                        reader.lines().collect(Collectors.joining("\n"))
                    }
                }
            }
            .orElse("")
}
