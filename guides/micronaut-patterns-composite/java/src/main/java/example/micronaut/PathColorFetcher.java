package example.micronaut;

import io.micronaut.http.HttpRequest;
import jakarta.inject.Singleton;

import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

@Singleton // <1>
public class PathColorFetcher implements ColorFetcher {

    private final String[] COLORS = {
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
    };

    @Override
    public Optional<String> favouriteColor(HttpRequest<?> request) {
        return Stream.of(COLORS)
                .filter(c -> request.getPath().contains(c.toLowerCase(Locale.ROOT)))
                .map(String::toLowerCase)
                .findFirst();
    }

    @Override
    public int getOrder() { // <2>
        return 20;
    }
}
