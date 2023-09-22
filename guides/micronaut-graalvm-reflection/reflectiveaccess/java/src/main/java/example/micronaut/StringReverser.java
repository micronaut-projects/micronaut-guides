package example.micronaut;

import io.micronaut.core.annotation.ReflectiveAccess;

public class StringReverser {

    @ReflectiveAccess // <1>
    static String reverse(String input) {
        return new StringBuilder(input).reverse().toString();
    }
}
