package example.micronaut;

import io.micronaut.core.io.ResourceLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public interface TestUtils {

    ResourceLoader getResourceLoader();

    default Optional<String> getResourceAsString(String path) {
        Optional<InputStream> inputStreamOptional = getResourceLoader().getResourceAsStream(path);
        if (!inputStreamOptional.isPresent()) {
            return Optional.empty();
        }
        try {
            return Optional.of(toString(inputStreamOptional.get()));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    default String toString(InputStream inputStream) throws IOException {
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (inputStream, StandardCharsets.UTF_8))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        return textBuilder.toString();
    }
}
