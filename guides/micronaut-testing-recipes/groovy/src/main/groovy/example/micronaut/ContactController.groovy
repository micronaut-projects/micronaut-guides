package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.views.View

@CompileStatic
@Controller("/contacts") // <1>
class ContactController {

    private final ContactRepository contactRepository

    ContactController(ContactRepository contactRepository) { // <2>
        this.contactRepository = contactRepository
    }

    @Secured(SecurityRule.IS_ANONYMOUS) // <3>
    @Produces(MediaType.TEXT_HTML) // <4>
    @View("contacts/list") // <5>
    @Get // <6>
    Map<String, Object> list() {
        [contacts: contactRepository.findAll()] as Map<String, Object>
    }
}
