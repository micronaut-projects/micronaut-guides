package example.micronaut

import io.micronaut.test.support.TestPropertyProvider
import org.junit.jupiter.api.io.TempDir
import java.io.File

abstract class BaseTest: TestPropertyProvider {

    companion object {
        @TempDir
        @JvmField
        var tempDir: File? = null
    }

    override fun getProperties(): MutableMap<String, String> {
        return mutableMapOf(
            "microstream.storage.main.storage-directory" to tempDir!!.absolutePath
        )
    }
}