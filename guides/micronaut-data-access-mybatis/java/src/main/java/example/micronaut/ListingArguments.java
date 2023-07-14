package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.http.uri.UriBuilder;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.net.URI;
import java.util.Optional;

@Serdeable
public class ListingArguments {

    @PositiveOrZero
    private Integer offset = 0;

    @Nullable
    @Positive
    private Integer max;

    @Nullable
    @Pattern(regexp = "id|name")
    private String sort;

    @Pattern(regexp = "asc|ASC|desc|DESC")
    @Nullable
    private String order;

    public ListingArguments(Integer offset, @Nullable Integer max, @Nullable String sort, @Nullable String order) {
        this.offset = offset;
        this.max = max;
        this.sort = sort;
        this.order = order;
    }

    @Nullable
    public Integer getOffset() {
        return offset;
    }

    public void setOffset(@Nullable Integer offset) {
        this.offset = offset;
    }

    @Nullable
    public Integer getMax() {
        return max;
    }

    public void setMax(@Nullable Integer max) {
        this.max = max;
    }

    @Nullable
    public String getSort() {
        return sort;
    }

    public void setSort(@Nullable String sort) {
        this.sort = sort;
    }

    @Nullable
    public String getOrder() {
        return order;
    }

    public void setOrder(@Nullable String order) {
        this.order = order;
    }

    @NonNull
    public static Builder builder() {
        return new Builder();
    }

    public URI of(UriBuilder uriBuilder) {
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
        return uriBuilder.build();
    }

    public static final class Builder {
        private Integer offset;

        @Nullable
        private Integer max;

        @Nullable
        private String sort;

        @Nullable
        private String order;
        private Builder() {
        }

        @NonNull
        public Builder max(int max) {
            this.max = max;
            return this;
        }

        @NonNull
        public Builder sort(String sort) {
            this.sort = sort;
            return this;
        }

        @NonNull
        public Builder order(String order) {
            this.order = order;
            return this;
        }

        @NonNull
        public Builder offset(int offset) {
            this.offset = offset;
            return this;
        }

        @NonNull
        public ListingArguments build() {
            return new ListingArguments(Optional.ofNullable(offset).orElse(0), max, sort, order);
        }
    }
}
