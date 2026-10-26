package example.micronaut

import io.micronaut.core.type.Argument
import io.micronaut.json.JsonMapper
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false) // <1>
class ContactJsonDeserializationSpec extends Specification {

    @Inject
    JsonMapper jsonMapper // <2>

    void "Contact JSON deserialization"() {
        given:
        Contact expected = new Contact("Sergio", "del Amo", "Object Computing")

        when:
        Contact contact = jsonMapper.readValue('{"firstName":"Sergio","lastName":"del Amo","company":"Object Computing"}',
                Argument.of(Contact)) // <3>

        then:
        contact == expected
    }
}
