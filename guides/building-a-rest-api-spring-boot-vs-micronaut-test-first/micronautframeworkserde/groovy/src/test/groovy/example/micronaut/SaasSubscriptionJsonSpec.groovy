package example.micronaut

import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import io.micronaut.core.io.ResourceLoader
import io.micronaut.json.JsonMapper
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false) // <1>
class SaasSubscriptionJsonSpec extends Specification {

    @Inject // <2>
    JsonMapper json

    @Inject
    ResourceLoader resourceLoader

    void saasSubscriptionSerializationTest() {
        given:
        SaasSubscription subscription = new SaasSubscription(99L, "Professional", 4900)
        String expected = getResourceAsString("expected.json")

        when:
        String result = json.writeValueAsString(subscription)
        DocumentContext documentContext = JsonPath.parse(result)
        Number id = documentContext.read('$.id')
        String name = documentContext.read('$.name')
        Number cents = documentContext.read('$.cents')

        then:
        result.replaceAll(/\s/, "") == expected.replaceAll(/\s/, "")
        id == 99
        name == "Professional"
        cents == 4900
    }

    void saasSubscriptionDeserializationTest() {
        given:
        String expected = """
           {
               "id":100,
               "name": "Advanced",
               "cents":2900
           }
           """

        expect:
        json.readValue(expected, SaasSubscription) == new SaasSubscription(100L, "Advanced", 2900)
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
