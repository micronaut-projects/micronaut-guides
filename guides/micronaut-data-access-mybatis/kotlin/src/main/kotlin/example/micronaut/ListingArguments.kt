package example.micronaut

import io.micronaut.core.annotation.Introspected
import io.micronaut.http.uri.UriBuilder
import java.net.URI
import java.util.Optional
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

@Introspected
class ListingArguments {

    @PositiveOrZero
    private var offset: Int? = 0

    @Positive
    private var max: Int? = null

    @Pattern(regexp = "id|name")
    private var sort: String? = null

    @Pattern(regexp = "asc|ASC|desc|DESC")
    private var order: String? = null

    fun getOffset(): Optional<Int> = Optional.ofNullable(offset)

    fun setOffset(offset: Int?) {
        this.offset = offset
    }

    fun getMax(): Optional<Int> = Optional.ofNullable(max)

    fun setMax(max: Int?) {
        this.max = max
    }

    fun getSort(): Optional<String> = Optional.ofNullable(sort)

    fun setSort(sort: String?) {
        this.sort = sort
    }

    fun getOrder(): Optional<String> = Optional.ofNullable(order)

    fun setOrder(order: String?) {
        this.order = order
    }

    fun of(uriBuilder: UriBuilder): URI {
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
        return uriBuilder.build()
    }

    class Builder {

        private val args = ListingArguments()

        fun max(max: Int): Builder {
            args.setMax(max)
            return this
        }

        fun sort(sort: String?): Builder {
            args.setSort(sort)
            return this
        }

        fun order(order: String?): Builder {
            args.setOrder(order)
            return this
        }

        fun offset(offset: Int): Builder {
            args.setOffset(offset)
            return this
        }

        fun build(): ListingArguments = args
    }

    companion object {
        fun builder(): Builder = Builder()
    }
}
