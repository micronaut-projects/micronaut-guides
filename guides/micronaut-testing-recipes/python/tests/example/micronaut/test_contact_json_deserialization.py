from micronaut.core.type import Argument
from micronaut.json import JsonMapper

from example.micronaut.contact import Contact


def test_contact_json_deserialization(my_context):
    json_mapper = my_context.getBean(JsonMapper)  # <1>
    contact = json_mapper.readValue(
        '{"firstName":"Sergio","lastName":"del Amo","company":"Object Computing"}',
        Argument.of(Contact),
    )  # <2>
    assert contact.firstName == "Sergio"
    assert contact.lastName == "del Amo"
    assert contact.company == "Object Computing"
