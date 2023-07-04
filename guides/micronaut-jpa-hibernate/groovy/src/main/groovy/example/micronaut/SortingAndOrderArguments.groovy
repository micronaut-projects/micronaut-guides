package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Nullable
import io.micronaut.serde.annotation.Serdeable

import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero

@CompileStatic
@Serdeable
class SortingAndOrderArguments {

    @Nullable
    @PositiveOrZero // <1>
    Integer offset

    @Nullable
    @Positive // <1>
    Integer max

    @Nullable
    @Pattern(regexp = 'id|name')  // <1>
    String sort

    @Nullable
    @Pattern(regexp = 'asc|ASC|desc|DESC')  // <1>
    String order
}
