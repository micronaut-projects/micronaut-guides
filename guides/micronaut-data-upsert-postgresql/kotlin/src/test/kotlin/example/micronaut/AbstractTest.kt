package example.micronaut

import io.micronaut.test.support.TestPropertyProvider
import org.testcontainers.containers.PostgreSQLContainer

abstract class AbstractTest : TestPropertyProvider { // <1>

    companion object {
        val postgres = PostgreSQLContainer<Nothing>("postgres:15.3-alpine") // <2>
    }

    override fun getProperties(): Map<String, String> {
        if (!postgres.isRunning) {
            postgres.start() // <3>
        }
        return mapOf(
            "datasources.default.driver-class-name" to "org.postgresql.Driver", // <4>
            "datasources.default.url" to postgres.jdbcUrl,
            "datasources.default.username" to postgres.username,
            "datasources.default.password" to postgres.password
        )
    }
}
