package example.micronaut

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import io.micronaut.serde.annotation.Serdeable

@Canonical
@CompileStatic
@Serdeable
class BookAnalytics {
    String bookIsbn
    long count
}
