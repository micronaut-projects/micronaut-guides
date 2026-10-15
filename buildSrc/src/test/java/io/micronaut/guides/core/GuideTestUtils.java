package io.micronaut.guides.core;

import java.util.List;
import java.util.Map;

public final class GuideTestUtils {
    private GuideTestUtils() {
    }

    public static Guide guideWithSlug(String slug) {
        return new Guide("", "", List.of(), List.of(), null, 0, 0, null, null, null, "", List.of(), List.of(), List.of(), null, List.of(), slug, null, "", Map.of(), List.of());
    }
}
