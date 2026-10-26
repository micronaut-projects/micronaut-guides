from micronaut.core.type import Argument
from micronaut.serde import SerdeIntrospections

from example.micronaut.contact import Contact


def test_contact_is_serializable_and_deserializable(my_context):
    introspections = my_context.getBean(SerdeIntrospections)  # <1>
    argument = Argument.of(Contact)
    introspections.getDeserializableIntrospection(argument)  # <2>
    introspections.getSerializableIntrospection(argument)  # <3>
