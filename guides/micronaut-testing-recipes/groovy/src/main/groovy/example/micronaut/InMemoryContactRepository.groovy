package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.NonNull
import jakarta.inject.Singleton

@CompileStatic
@Singleton // <1>
class InMemoryContactRepository implements ContactRepository {

    private final List<Contact> contacts = [
            new Contact("Sergio", "del Amo", "Object Computing"),
            new Contact("Graeme", "Rocher", "Oracle")
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
