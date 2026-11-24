from micronaut.core.beans import BeanIntrospection

from example.micronaut.contact import Contact


def test_contact_is_introspected():
    BeanIntrospection.getIntrospection(Contact)  # <1>
