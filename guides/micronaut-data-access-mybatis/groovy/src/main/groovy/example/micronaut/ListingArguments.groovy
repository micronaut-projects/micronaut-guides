package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Nullable
import io.micronaut.serde.annotation.Serdeable
import io.micronaut.http.uri.UriBuilder

import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

@CompileStatic
@Serdeable
class ListingArguments {

    @PositiveOrZero
    private Integer offset = 0

    @Nullable
    @Positive
    private Integer max

    @Nullable
    @Pattern(regexp = "id|name")
    private String sort

    @Pattern(regexp = "asc|ASC|desc|DESC")
    @Nullable
    private String order

    private ListingArguments() {
    }

    ListingArguments(Integer offset, @Nullable Integer max, @Nullable String sort, @Nullable String order) {
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

    static Builder builder() {
        new Builder()
    }

    URI of(UriBuilder uriBuilder) {
        if (max != null) {
            uriBuilder.queryParam("max", max)
        }
        if (order != null) {
            uriBuilder.queryParam("order", order)
        }
        if (offset != null) {
            uriBuilder.queryParam("offset", offset)
        }
        if (sort != null) {
            uriBuilder.queryParam("sort", sort)
        }
        uriBuilder.build()
    }

    static final class Builder {
        private final ListingArguments args = new ListingArguments()

        private Builder() {
        }

        Builder max(int max) {
            args.max = max
            this
        }

        Builder sort(String sort) {
            args.sort = sort
            this
        }

        Builder order(String order) {
            args.order = order
            this
        }

        Builder offset(int offset) {
            args.offset = offset
            this
        }

        ListingArguments build() {
            args
        }
    }
}
