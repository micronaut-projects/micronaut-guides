package example.micronaut

import io.micronaut.serde.annotation.Serdeable
import io.micronaut.http.uri.UriBuilder
import java.net.URI
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

        fun max(max: Int): Builder = apply { args.max = max }
        fun sort(sort: String?): Builder = apply { args.sort = sort }
        fun order(order: String?): Builder = apply { args.order = order }
        fun offset(offset: Int): Builder = apply { args.offset = offset }
        fun build(): ListingArguments = args
    }

    companion object {
        fun builder(): Builder = Builder()
    }
}
