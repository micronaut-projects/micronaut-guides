package example.micronaut

import io.micronaut.test.support.TestPropertyProvider
import spock.lang.Specification
import java.nio.file.Files
import java.nio.file.Path
import io.micronaut.core.annotation.NonNull

abstract class BaseSpec extends Specification implements TestPropertyProvider {

    @Override
    @NonNull
    Map<String, String> getProperties() {
        Path tempDir = Files.createTempDirectory('microstream')
        ["microstream.storage.main.storage-directory": tempDir.toString()]
    }
}
