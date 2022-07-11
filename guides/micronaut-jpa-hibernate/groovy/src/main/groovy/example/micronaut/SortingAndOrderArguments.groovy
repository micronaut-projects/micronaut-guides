package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Introspected
import io.micronaut.core.annotation.Nullable

import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

@CompileStatic
@Introspected
class SortingAndOrderArguments {

    @Nullable
    @PositiveOrZero // <1>
    private Integer offset

    @Nullable
    @Positive // <1>
    private Integer max

    @Nullable
    @Pattern(regexp = 'id|name')  // <1>
    private String sort

    @Nullable
    @Pattern(regexp = 'asc|ASC|desc|DESC')  // <1>
    private String order

    Optional<Integer> getOffset() {
        Optional.ofNullable(offset)
    }

    void setOffset(@Nullable Integer offset) {
        this.offset = offset
    }

    Optional<Integer> getMax() {
        Optional.ofNullable(max)
    }

    void setMax(@Nullable Integer max) {
        this.max = max
    }

    Optional<String> getSort() {
        Optional.ofNullable(sort)
    }

    void setSort(@Nullable String sort) {
        this.sort = sort
    }

    Optional<String> getOrder() {
        Optional.ofNullable(order)
    }

    void setOrder(@Nullable String order) {
        this.order = order
    }
}
