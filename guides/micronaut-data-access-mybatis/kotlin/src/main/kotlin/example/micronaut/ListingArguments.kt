package example.micronaut

import io.micronaut.serde.annotation.Serdeable
import io.micronaut.http.uri.UriBuilder
import java.net.URI
import java.util.Optional
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero

@Serdeable
class ListingArguments(
    @field:PositiveOrZero var offset: Int? = 0,
    @field:Positive var max: Int? = null,
    @field:Pattern(regexp = "id|name") var sort: String? = null,
    @field:Pattern(regexp = "asc|ASC|desc|DESC") var order: String? = null
) {
    fun getOffset(): Optional<Int> = Optional.ofNullable(offset)

    fun getMax(): Optional<Int> = Optional.ofNullable(max)

    fun getSort(): Optional<String> = Optional.ofNullable(sort)

    fun getOrder(): Optional<String> = Optional.ofNullable(order)

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
            args.max = max
            return this
        }

        fun sort(sort: String?): Builder {
            args.sort = sort
            return this
        }

        fun order(order: String?): Builder {
            args.order = order
            return this
        }

        fun offset(offset: Int): Builder {
            args.offset = offset
            return this
        }

        fun build(): ListingArguments = args
    }

    companion object {
        fun builder(): Builder = Builder()
    }
}
