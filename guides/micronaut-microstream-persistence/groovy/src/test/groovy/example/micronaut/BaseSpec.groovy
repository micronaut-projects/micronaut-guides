package example.micronaut

import io.micronaut.test.support.TestPropertyProvider
import spock.lang.Specification
import javax.validation.constraints.NotNull
import java.nio.file.Files
import java.nio.file.Path

abstract class BaseSpec extends Specification implements TestPropertyProvider {

    @Override
    @NotNull
    Map<String, String> getProperties() {
        Path tempDir = Files.createTempDirectory('microstream')
        ["microstream.storage.main.storage-directory": tempDir.toString()]
    }
}
