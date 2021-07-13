package example.micronaut

import io.micronaut.core.annotation.Introspected

@Introspected
data class BookAnalytics(val bookIsbn: String, val count: Long)
