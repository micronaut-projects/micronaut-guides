package example.micronaut

import io.micronaut.core.annotation.Introspected
import groovy.transform.CompileStatic

@CompileStatic
@Introspected
class Book {
    String title
}
