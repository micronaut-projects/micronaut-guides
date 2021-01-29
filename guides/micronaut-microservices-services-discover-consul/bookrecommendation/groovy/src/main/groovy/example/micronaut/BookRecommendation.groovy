package example.micronaut

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.TupleConstructor

@TupleConstructor
@CompileStatic
@EqualsAndHashCode
class BookRecommendation {
    String name
}
