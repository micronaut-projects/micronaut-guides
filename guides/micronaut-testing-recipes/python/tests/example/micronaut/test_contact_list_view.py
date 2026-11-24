import java

from example.micronaut.contact import Contact

ThymeleafViewsRenderer = java.type("io.micronaut.views.thymeleaf.ThymeleafViewsRenderer")
HashMap = java.type("java.util.HashMap")
ArrayList = java.type("java.util.ArrayList")
StringWriter = java.type("java.io.StringWriter")


def test_contact_list_renders_contacts(my_context):
    views_renderer = my_context.getBean(ThymeleafViewsRenderer)  # <1>
    contacts = ArrayList()
    contacts.add(Contact("Tim", "Cook", "Apple Inc."))
    model = HashMap()
    model.put("contacts", contacts)
    writable = views_renderer.render("contacts/list", model, None)  # <2>
    writer = StringWriter()
    writable.writeTo(writer)
    html = writer.toString()  # <3>
    assert "<!DOCTYPE html>" in html
    assert "<td>Tim</td>" in html
    assert "<td>Apple Inc.</td>" in html
