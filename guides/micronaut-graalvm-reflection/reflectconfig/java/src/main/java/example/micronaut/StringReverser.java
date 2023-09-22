package example.micronaut;

public class StringReverser {

    static String reverse(String input) {
        return new StringBuilder(input).reverse().toString();
    }
}
