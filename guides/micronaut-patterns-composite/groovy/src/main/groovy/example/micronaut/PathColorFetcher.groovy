package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.HttpRequest
import jakarta.inject.Singleton

@CompileStatic
@Singleton // <1>
class PathColorFetcher implements ColorFetcher {

    private static final String[] COLORS = [
            'Red',
            'Blue',
            'Green',
            'Orange',
            'White',
            'Black',
            'Yellow',
            'Purple',
            'Silver',
            'Brown',
            'Gray',
            'Pink',
            'Olive',
            'Maroon',
            'Violet',
            'Charcoal',
            'Magenta',
            'Bronze',
            'Cream',
            'Gold',
            'Tan',
            'Teal',
            'Mustard',
            'Navy Blue',
            'Coral',
            'Burgundy',
            'Lavender',
            'Mauve',
            'Peach',
            'Rust',
            'Indigo',
            'Ruby',
            'Clay',
            'Cyan',
            'Azure',
            'Beige',
            'Turquoise',
            'Amber',
            'Mint'
    ]

    @Override
    Optional<String> favouriteColor(HttpRequest<?> request) {
        return Optional.ofNullable(
                COLORS.findAll { request.path.contains(it.toLowerCase(Locale.ROOT)) }
                      .collect { it.toLowerCase() }[0]
        )
    }

    @Override
    int getOrder() { // <2>
        return 20
    }
}
