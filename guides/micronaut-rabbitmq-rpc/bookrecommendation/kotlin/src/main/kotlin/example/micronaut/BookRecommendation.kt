package example.micronaut

import io.micronaut.serde.annotation.Serdeable

@Serdeable
class BookRecommendation(val name: String)
