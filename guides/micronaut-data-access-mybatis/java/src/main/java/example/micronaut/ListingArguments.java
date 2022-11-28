package example.micronaut;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.http.uri.UriBuilder;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
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

    private ListingArguments() {
    }

    public ListingArguments(Integer offset, @Nullable Integer max, @Nullable String sort, @Nullable String order) {
        this.offset = offset;
        this.max = max;
        this.sort = sort;
        this.order = order;
    }

    public Optional<Integer> getOffset() {
        return Optional.ofNullable(offset);
    }

    public void setOffset(@Nullable Integer offset) {
        this.offset = offset;
    }

    public Optional<Integer> getMax() {
        return Optional.ofNullable(max);
    }

    public void setMax(@Nullable Integer max) {
        this.max = max;
    }

    public Optional<String> getSort() {
        return Optional.ofNullable(sort);
    }

    public void setSort(@Nullable String sort) {
        this.sort = sort;
    }

    public Optional<String> getOrder() {
        return Optional.ofNullable(order);
    }

    public void setOrder(@Nullable String order) {
        this.order = order;
    }

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
        private final ListingArguments args = new ListingArguments();

        private Builder() {
        }

        public Builder max(int max) {
            args.setMax(max);
            return this;
        }

        public Builder sort(String sort) {
            args.setSort(sort);
            return this;
        }

        public Builder order(String order) {
            args.setOrder(order);
            return this;
        }

        public Builder offset(int offset) {
            args.setOffset(offset);
            return this;
        }

        public ListingArguments build() {
            return args;
        }
    }
}
