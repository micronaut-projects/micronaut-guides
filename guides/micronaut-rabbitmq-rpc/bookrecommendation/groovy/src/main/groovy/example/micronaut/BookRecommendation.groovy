package example.micronaut

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import io.micronaut.serde.annotation.Serdeable

@CompileStatic
@EqualsAndHashCode
@Serdeable
class BookRecommendation {

    final String name;

    BookRecommendation(String name) {
        this.name = name;
    }
}
