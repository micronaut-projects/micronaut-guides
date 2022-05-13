package example.micronaut

import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import spock.lang.Specification

import javax.validation.constraints.NotNull
import java.nio.file.Files
import java.nio.file.Path

abstract class BaseMicroStreamSpec extends Specification implements TestPropertyProvider {

    @Inject
    EmbeddedApplication<?> application

    @Inject
    FruitClient fruitClient

    @Inject
    @Client("/")
    HttpClient httpClient

    @Override
    @NotNull
    Map<String, String> getProperties() {
        Path tempDir = Files.createTempDirectory('microstream')
        println(tempDir)
        ["microstream.storage.main.storage-directory": tempDir.toString()]
    }
}
