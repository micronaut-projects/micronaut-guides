package example.micronaut.repository

import org.testcontainers.containers.OracleContainer

object Oracle {
    var oracle = OracleContainer("gvenzl/oracle-xe:21-slim-faststart")
        .withDatabaseName("testDB")
        .withUsername("testUser")
        .withPassword("testPassword")

    val configuration: Map<String, Any>
        get() {
            start()
            return java.util.Map.of<String, Any>(
                "datasources.default.url", oracle.jdbcUrl,
                "datasources.default.username", oracle.username,
                "datasources.default.password", oracle.password,
                "datasources.default.driver-class-name", oracle.driverClassName
            )
        }

    fun start() {
        if (!oracle.isRunning()) {
            oracle.start()
        }
    }

    fun stop() {
        if (oracle.isRunning()) {
            oracle.stop()
        }
    }

    fun close() {
        oracle.close()
    }
}
