package example.micronaut

import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import io.micronaut.core.annotation.NonNull
import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import spock.lang.Specification

@Property(name = "spec.name", value = "ContactControllerSpec") // <1>
@MicronautTest // <2>
class ContactControllerSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient httpClient // <3>

    void "contacts are rendered as HTML"() {
        given:
        BlockingHttpClient client = httpClient.toBlocking()
        HttpRequest<?> request = HttpRequest.GET("/contacts").accept(MediaType.TEXT_HTML) // <4>

        when:
        String html = client.retrieve(request, String) // <5>

        then:
        noExceptionThrown()
        html.contains("<!DOCTYPE html>")
        html.contains("Katherine")
        html.contains("Adams")
        html.contains("Tim")
        html.contains("Cook")
    }

    @Requires(property = "spec.name", value = "ContactControllerSpec") // <1>
    @Replaces(ContactRepository) // <6>
    @Singleton
    static class ContactRepositoryReplacement implements ContactRepository {

        private final List<Contact> contacts = [
                new Contact("Katherine", "Adams", "Apple Inc."),
                new Contact("Tim", "Cook", "Apple Inc.")
        ].asImmutable()

        @Override
        @NonNull
        List<Contact> findAll() {
            contacts
        }

        @Override
        @NonNull
        Optional<Contact> findByFirstName(@NonNull String firstName) {
            Optional.ofNullable(contacts.find { Contact contact -> contact.firstName == firstName })
        }
    }
}
