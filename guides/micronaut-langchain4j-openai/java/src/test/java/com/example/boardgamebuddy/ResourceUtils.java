package com.example.boardgamebuddy;

import io.micronaut.core.io.ResourceLoader;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public final class ResourceUtils {
    private ResourceUtils() {

    }

    @NonNull
    public static String classPathResource(@NonNull ResourceLoader resourceLoader,
                                    @NonNull String path) throws IOException {
        if (!path.startsWith("classpath:")) {
            path =  "classpath:" + path;
        }
        Optional<InputStream> inputStreamOptional = resourceLoader.getResourceAsStream(path);
        assertTrue(inputStreamOptional.isPresent());
        InputStream inputStream = inputStreamOptional.get();
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }
}
