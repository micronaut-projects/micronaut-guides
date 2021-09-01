package example.micronaut

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import io.micronaut.core.annotation.Introspected

@CompileStatic
@EqualsAndHashCode
@Introspected
class BookRecommendation {

    final String name;

    BookRecommendation(String name) {
        this.name = name;
    }
}
