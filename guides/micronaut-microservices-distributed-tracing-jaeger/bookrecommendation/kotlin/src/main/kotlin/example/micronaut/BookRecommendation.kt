package example.micronaut
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
data class BookRecommendation(val name: String)