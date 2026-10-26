package example.micronaut

import io.micronaut.core.io.Writable
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.views.thymeleaf.ThymeleafViewsRenderer
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false) // <1>
class ContactListViewSpec extends Specification {

    @Inject
    ThymeleafViewsRenderer<Map<String, Object>> viewsRenderer // <2>

    void "contact list renders contacts"() {
        given:
        Map<String, Object> model = [contacts: [new Contact("Tim", "Cook", "Apple Inc.")]] as Map<String, Object>

        when:
        Writable writable = viewsRenderer.render("contacts/list", model, null) // <3>
        String html = WritableUtils.writableToString(writable) // <4>

        then:
        html.contains("<!DOCTYPE html>")
        html.contains("<td>Tim</td>")
        html.contains("<td>Apple Inc.</td>")
    }
}
