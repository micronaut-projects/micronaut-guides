package example.micronaut

import io.micronaut.http.HttpRequest
import jakarta.inject.Singleton
import java.util.Locale
import java.util.Optional
import java.util.stream.Stream

@Singleton // <1>
class PathColorFetcher : ColorFetcher {

    override fun favouriteColor(request: HttpRequest<*>): Optional<String> {
        return Stream.of(*COLORS)
            .filter { request.path.contains(it.lowercase()) }
            .map { it.lowercase(Locale.getDefault()) }
            .findFirst()
    }

    override fun getOrder(): Int = 20 // <2>

    companion object {
        private val COLORS = arrayOf(
            "Red",
            "Blue",
            "Green",
            "Orange",
            "White",
            "Black",
            "Yellow",
            "Purple",
            "Silver",
            "Brown",
            "Gray",
            "Pink",
            "Olive",
            "Maroon",
            "Violet",
            "Charcoal",
            "Magenta",
            "Bronze",
            "Cream",
            "Gold",
            "Tan",
            "Teal",
            "Mustard",
            "Navy Blue",
            "Coral",
            "Burgundy",
            "Lavender",
            "Mauve",
            "Peach",
            "Rust",
            "Indigo",
            "Ruby",
            "Clay",
            "Cyan",
            "Azure",
            "Beige",
            "Turquoise",
            "Amber",
            "Mint"
        )
    }
}
