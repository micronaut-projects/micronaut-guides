package example.micronaut

import groovy.transform.EqualsAndHashCode
import groovy.transform.TupleConstructor
import io.micronaut.core.annotation.Introspected
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable

@EqualsAndHashCode
@TupleConstructor
@Introspected // <1>
class ContactPreview {
    @NonNull
    Long id

    @Nullable
    String firstName

    @Nullable
    String lastName
}
