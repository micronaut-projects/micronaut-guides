package example.micronaut

import io.micronaut.core.annotation.Introspected

@Introspected
class Author {

    final String id
    final String firstName
    final String lastName

    Author(String id, String firstName, String lastName) {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
    }

}
