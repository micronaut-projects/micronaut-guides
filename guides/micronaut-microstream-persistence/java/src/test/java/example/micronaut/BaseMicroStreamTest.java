package example.micronaut;

import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.Collections;
import java.util.Map;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseMicroStreamTest implements TestPropertyProvider {

    @TempDir
    static File tempDir;

    @Inject
    EmbeddedApplication<?> application;

    @Inject
    FruitClient fruitClient;

    @Inject
    @Client("/")
    HttpClient httpClient;

    @Override
    @NotNull
    public Map<String, String> getProperties() {
        return Collections.singletonMap(
                "microstream.storage.main.storage-directory", tempDir.getAbsolutePath()
        );
    }
}
