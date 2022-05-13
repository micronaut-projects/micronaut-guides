package example.micronaut

import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.io.TempDir
import java.io.File

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseMicroStreamTest: TestPropertyProvider {

    companion object {
        @TempDir
        @JvmField
        var tempDir: File? = null
    }

    @Inject
    lateinit var application: EmbeddedApplication<*>

    @Inject
    lateinit var fruitClient: FruitClient

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    override fun getProperties(): MutableMap<String, String> {
        return mutableMapOf(
            "microstream.storage.main.storage-directory" to tempDir!!.absolutePath
        )
    }
}