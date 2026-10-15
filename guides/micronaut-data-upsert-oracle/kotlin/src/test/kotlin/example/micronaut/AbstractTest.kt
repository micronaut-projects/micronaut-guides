package example.micronaut

import io.micronaut.test.support.TestPropertyProvider
import org.testcontainers.oracle.OracleContainer
import org.testcontainers.utility.DockerImageName

abstract class AbstractTest : TestPropertyProvider { // <1>

    companion object {
        const val IMAGE_NAME = "gvenzl/oracle-free:23.26.1-faststart"

        val container = OracleContainer(DockerImageName.parse(IMAGE_NAME)) // <2>
    }

    override fun getProperties(): Map<String, String> {
        if (!container.isRunning) {
            container.start() // <3>
        }
        return mapOf( // <4>
            "datasources.default.driver-class-name" to "oracle.jdbc.OracleDriver",
            "datasources.default.url" to container.jdbcUrl,
            "datasources.default.username" to container.username,
            "datasources.default.password" to container.password
        )
    }
}
