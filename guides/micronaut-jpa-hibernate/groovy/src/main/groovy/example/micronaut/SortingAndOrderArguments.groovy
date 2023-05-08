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

    SortingAndOrderArguments(@Nullable Integer offset, @Nullable Integer max, @Nullable String sort, @Nullable String order) {
        this.offset = offset
        this.max = max
        this.sort = sort
        this.order = order
    }

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
