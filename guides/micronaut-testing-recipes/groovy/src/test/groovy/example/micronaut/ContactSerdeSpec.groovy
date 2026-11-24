package example.micronaut

import io.micronaut.core.type.Argument
import io.micronaut.serde.SerdeIntrospections
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false) // <1>
class ContactSerdeSpec extends Specification {

    @Inject
    SerdeIntrospections serdeIntrospections // <2>

    void "Contact is deserializable"() {
        when:
        serdeIntrospections.getDeserializableIntrospection(Argument.of(Contact)) // <3>

        then:
        noExceptionThrown()
    }

    void "Contact is serializable"() {
        when:
        serdeIntrospections.getSerializableIntrospection(Argument.of(Contact)) // <4>

        then:
        noExceptionThrown()
    }
}
