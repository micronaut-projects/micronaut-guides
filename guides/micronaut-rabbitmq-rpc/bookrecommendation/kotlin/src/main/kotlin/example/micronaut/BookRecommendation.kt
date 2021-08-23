package example.micronaut

import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Introspected

@Introspected
class BookRecommendation @Creator constructor(val name: String)
