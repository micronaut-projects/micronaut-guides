package example.micronaut

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class BookAnalytics(val bookIsbn: String, val count: Long)
