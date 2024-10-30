package io.micronaut.guides.core.asciidoc;

import java.util.List;
import java.util.Optional;

public record PlacheholderMacro(String name, String target) {
    private static final String MACRO_NAME_SEPARATOR = ":";
    private static final String MACRO_BRACKET = "@";

    public static Optional<PlacheholderMacro> of(String name, String str) {
        if (str.startsWith(MACRO_BRACKET) && str.endsWith(name+MACRO_BRACKET)) {
            String target = str.replace(name+MACRO_BRACKET,"").replace(MACRO_BRACKET,"").replace(MACRO_NAME_SEPARATOR,"");
            return Optional.of(new PlacheholderMacro(name, target));
        }
        return Optional.empty();
    }
}
