package io.micronaut.guides.core.asciidoc;

import io.micronaut.core.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public record Attribute(String key, List<String> values) {
    private static final String ATTRIBUTE_SEPARATOR = ",";
    private static final String VALUE_SEPARATOR = ";";
    private static final String KEY_VALUE_SEPARATOR = "=";

    @NonNull
    public static List<Attribute> of(@NonNull String str) {
        List<Attribute> result = new ArrayList<>();
        String[] arr = str.split(ATTRIBUTE_SEPARATOR);
        for (String attributeString : arr) {
            String[] attributeComponents = attributeString.split(KEY_VALUE_SEPARATOR);
            if (attributeComponents.length == 2) {
                String key = attributeComponents[0].trim();
                String value = attributeComponents[1].trim();
                String[] values = value.split(VALUE_SEPARATOR);
                result.add(new Attribute(key, List.of(values)));
            }
        }
        return result;
    }
}
