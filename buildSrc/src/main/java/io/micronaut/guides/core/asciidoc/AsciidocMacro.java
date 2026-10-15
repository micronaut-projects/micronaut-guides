package io.micronaut.guides.core.asciidoc;

import java.util.List;
import java.util.Optional;

public record AsciidocMacro(String name, String target, List<Attribute> attributes) {
    public static final String MACRO_NAME_SEPARATOR = ":";
    public static final String ATTRIBUTE_SEPARATOR = ",";
    private static final String MACRO_OPEN_BRACKET = "[";
    private static final String MACRO_CLOSE_BRACKET = "]";

    public static Optional<AsciidocMacro> of(String name, String str) {
        int bracketIndex = str.indexOf(MACRO_OPEN_BRACKET);
        int closingBracketIndex = str.indexOf(MACRO_CLOSE_BRACKET);
        if (str.startsWith(name + MACRO_NAME_SEPARATOR) && bracketIndex != -1 && closingBracketIndex != -1) {
            String target = str.substring((name + MACRO_NAME_SEPARATOR).length(), bracketIndex);
            String attributeLine = str.substring(bracketIndex + 1, closingBracketIndex);
            List<Attribute> attributes = Attribute.of(attributeLine);
            return Optional.of(new AsciidocMacro(name, target, attributes));
        }
        return Optional.empty();
    }
}
