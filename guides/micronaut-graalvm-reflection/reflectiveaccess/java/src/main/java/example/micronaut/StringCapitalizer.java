package example.micronaut;

import io.micronaut.core.annotation.ReflectiveAccess;

public class StringCapitalizer {
    @ReflectiveAccess // <1>
    static String capitalize(String input) {
        return input.toUpperCase();
    }
}
