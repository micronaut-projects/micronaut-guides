package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.serde.annotation.Serdeable
import io.micronaut.http.uri.UriBuilder

import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import groovy.transform.CompileStatic

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

    ListingArguments(Integer offset, @Nullable Integer max, @Nullable String sort, @Nullable String order) {
        this.offset = offset
        this.max = max
        this.sort = sort
        this.order = order
    }

    Integer getOffset() {
        offset
    }

    void setOffset(@Nullable Integer offset) {
        this.offset = offset
    }

    Integer getMax() {
        max
    }

    void setMax(@Nullable Integer max) {
        this.max = max
    }

    String getSort() {
        sort
    }

    void setSort(@Nullable String sort) {
        this.sort = sort
    }

    String getOrder() {
        order
    }

    void setOrder(@Nullable String order) {
        this.order = order
    }

    @NonNull
    static Builder builder() {
        return new Builder()
    }

    URI of(UriBuilder uriBuilder) {
        if (max != null) {
            uriBuilder.queryParam("max", max);
        }
        if (order != null) {
            uriBuilder.queryParam("order", order);
        }
        if (offset != null) {
            uriBuilder.queryParam("offset", offset);
        }
        if (sort != null) {
            uriBuilder.queryParam("sort", sort);
        }
        uriBuilder.build()
    }

    static final class Builder {
        private Integer offset

        @Nullable
        private Integer max

        @Nullable
        private String sort

        @Nullable
        private String order

        private Builder() {
        }

        @NonNull
        Builder max(int max) {
            this.max = max
            this
        }

        @NonNull
        Builder sort(String sort) {
            this.sort = sort
            this
        }

        @NonNull
        Builder order(String order) {
            this.order = order
            this
        }

        @NonNull
        Builder offset(int offset) {
            this.offset = offset
            this
        }

        @NonNull
        ListingArguments build() {
            new ListingArguments(Optional.ofNullable(offset).orElse(0), max, sort, order)
        }
    }
}
