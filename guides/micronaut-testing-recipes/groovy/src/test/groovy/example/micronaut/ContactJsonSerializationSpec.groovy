package example.micronaut

import io.micronaut.json.JsonMapper
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false) // <1>
class ContactJsonSerializationSpec extends Specification {

    @Inject
    JsonMapper jsonMapper // <2>

    void "Contact JSON serialization"() {
        given:
        Contact contact = new Contact("Sergio", "del Amo", "Object Computing")

        when:
        String json = jsonMapper.writeValueAsString(contact) // <3>

        then:
        json == '{"firstName":"Sergio","lastName":"del Amo","company":"Object Computing"}'
    }
}
