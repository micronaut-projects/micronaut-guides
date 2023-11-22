package example.micronaut;

import io.micronaut.test.support.TestPropertyProvider;
import org.junit.jupiter.api.io.TempDir;

import jakarta.validation.constraints.NotNull;
import java.io.File;
import java.util.Collections;
import java.util.Map;
import io.micronaut.core.annotation.NonNull;

abstract class BaseTest implements TestPropertyProvider {

    @TempDir
    static File tempDir;

    @Override
    @NonNull
    public Map<String, String> getProperties() {
        return Collections.singletonMap(
                "microstream.storage.main.storage-directory", tempDir.getAbsolutePath()
        );
    }
}
