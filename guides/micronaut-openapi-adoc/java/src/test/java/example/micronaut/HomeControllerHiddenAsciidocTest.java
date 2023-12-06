package example.micronaut;

import io.micronaut.core.io.ResourceLoader;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false) // <1>
class HomeControllerHiddenAsciidocTest {

    @Test
    void homeControllerIsHidden(ResourceLoader resourceLoader) throws IOException {
        Optional<InputStream> adocInputStream = resourceLoader.getResourceAsStream("META-INF/swagger/micronaut-guides-1.0.adoc");
        assertTrue(adocInputStream.isPresent());
        String adoc = new String(adocInputStream.get().readAllBytes(), StandardCharsets.UTF_8);
        assertFalse(adoc.contains("=== __GET__ `/`"));
    }
}
