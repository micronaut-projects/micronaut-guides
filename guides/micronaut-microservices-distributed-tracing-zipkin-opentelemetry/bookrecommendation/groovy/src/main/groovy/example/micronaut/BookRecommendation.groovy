package example.micronaut

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.TupleConstructor
import io.micronaut.core.annotation.Introspected

@TupleConstructor
@CompileStatic
@EqualsAndHashCode
@Introspected
class BookRecommendation {
    String name
}
