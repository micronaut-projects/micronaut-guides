package example.micronaut

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Introspected

@Canonical
@CompileStatic
@Introspected
class Book {
    String isbn
    String name
}
