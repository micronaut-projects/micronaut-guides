package example.micronaut

import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import io.micronaut.core.io.ResourceLoader
import io.micronaut.json.JsonMapper
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

import java.io.InputStream
import java.nio.charset.StandardCharsets

@MicronautTest(startApplication = false) // <1>
class SaasSubscriptionJsonSpec extends Specification {

    @Inject
    JsonMapper json

    @Inject
    ResourceLoader resourceLoader

    void "SaaS subscription serialization"() {
        given:
        SaasSubscription subscription = new SaasSubscription(id: 99L, name: 'Professional', cents: 4900)
        String expected = getResourceAsString('expected.json')

        when:
        String result = json.writeValueAsString(subscription)
        DocumentContext documentContext = JsonPath.parse(result)
        Number id = documentContext.read('$.id')
        String name = documentContext.read('$.name')
        Number cents = documentContext.read('$.cents')

        then:
        result.replaceAll(/\s+/, '') == expected.replaceAll(/\s+/, '')
        id
        id == 99
        name
        name == 'Professional'
        cents
        cents == 4900
    }

    void "SaaS subscription deserialization"() {
        given:
        String expected = '''
           {
               "id":100,
               "name": "Advanced",
               "cents":2900
           }
           '''

        when:
        SaasSubscription subscription = json.readValue(expected, SaasSubscription)

        then:
        subscription == new SaasSubscription(id: 100L, name: 'Advanced', cents: 2900)
        subscription.id == 100
        subscription.name == 'Advanced'
        subscription.cents == 2900
    }

    private String getResourceAsString(String resourceName) {
        resourceLoader.getResourceAsStream(resourceName)
                .map { InputStream stream ->
                    try (InputStream inputStream = stream) {
                        return inputStream.getText(StandardCharsets.UTF_8.name())
                    }
                }
                .orElse('')
    }
}
