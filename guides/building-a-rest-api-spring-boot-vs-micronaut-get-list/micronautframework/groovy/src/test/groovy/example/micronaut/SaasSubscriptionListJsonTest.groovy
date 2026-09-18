package example.micronaut

import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import io.micronaut.core.io.ResourceLoader
import io.micronaut.json.JsonMapper
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Test

import java.nio.charset.StandardCharsets

import static org.assertj.core.api.Assertions.assertThat

@MicronautTest(startApplication = false) // <1>
class SaasSubscriptionListJsonTest {

    @Test
    void saasSubscriptionSerializationTest(JsonMapper json, ResourceLoader resourceLoader) { // <2>
        SaasSubscription subscription = new SaasSubscription(id: 99L, name: 'Professional', cents: 4900)
        String expected = getResourceAsString(resourceLoader, 'expected.json')
        String result = json.writeValueAsString(subscription)
        assertThat(result).isEqualToIgnoringWhitespace(expected)
        DocumentContext documentContext = JsonPath.parse(result)
        Number id = documentContext.read('$.id')
        assertThat(id)
                .isNotNull()
                .isEqualTo(99)

        String name = documentContext.read('$.name')
        assertThat(name)
                .isNotNull()
                .isEqualTo('Professional')

        Number cents = documentContext.read('$.cents')
        assertThat(cents)
                .isNotNull()
                .isEqualTo(4900)
    }

    @Test
    void saasSubscriptionDeserializationTest(JsonMapper json) { // <2>
        String expected = '''
           {
               "id":100,
               "name": "Advanced",
               "cents":2900
           }
           '''
        assertThat(json.readValue(expected, SaasSubscription))
                .isEqualTo(new SaasSubscription(id: 100L, name: 'Advanced', cents: 2900))
        assertThat(json.readValue(expected, SaasSubscription).id).isEqualTo(100)
        assertThat(json.readValue(expected, SaasSubscription).name).isEqualTo('Advanced')
        assertThat(json.readValue(expected, SaasSubscription).cents).isEqualTo(2900)
    }

    private static String getResourceAsString(ResourceLoader resourceLoader, String resourceName) {
        resourceLoader.getResourceAsStream(resourceName)
                .map { InputStream stream ->
                    try (InputStream inputStream = stream) {
                        return inputStream.getText(StandardCharsets.UTF_8.name())
                    }
                }
                .orElse('')
    }
}
