from micronaut.json import JsonMapper

from example.micronaut.contact import Contact


def test_contact_json_serialization(my_context):
    json_mapper = my_context.getBean(JsonMapper)  # <1>
    contact = Contact("Sergio", "del Amo", "Object Computing")
    json = json_mapper.writeValueAsString(contact)  # <2>
    assert json == '{"firstName":"Sergio","lastName":"del Amo","company":"Object Computing"}'
