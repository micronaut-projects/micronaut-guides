package example.micronaut

import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.TestInstance
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName

@MicronautTest // <1>
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // <2>
open class BaseMysqlTest : TestPropertyProvider { // <3>
    private var mysqlContainer: GenericContainer<*>? = null

    @Inject
    open var application: EmbeddedApplication<*>? = null

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient // <4>

    open fun startMySQL() {
        if (mysqlContainer == null) {
            mysqlContainer = GenericContainer(DockerImageName.parse("mysql:8.0.29"))
                .withExposedPorts(3306)
                .withEnv("MYSQL_ROOT_PASSWORD", "my-secret-pw")
                .withEnv("MYSQL_DATABASE", "db")
                .waitingFor(Wait.forLogMessage(".*/usr/sbin/mysqld: ready for connections.*\\n", 2))
        }
        if (!mysqlContainer!!.isRunning) {
            mysqlContainer!!.start()
        }
    }

    open fun getMySQLDbUri(): String {
        if (mysqlContainer == null || !mysqlContainer!!.isRunning) {
            startMySQL()
        }
        return "jdbc:mysql://localhost:" + mysqlContainer!!.getMappedPort(3306) + "/db"
    }

    override fun getProperties(): MutableMap<String, String> { // <5>
        return mutableMapOf(
            Pair("jpa.default.properties.hibernate.connection.url", getMySQLDbUri()),
            Pair("datasources.migration.url", getMySQLDbUri()),
            Pair("datasources.migration.driverClassName", "com.mysql.cj.jdbc.Driver")
        )
    }

    @AfterAll
    open fun stop() {
        mysqlContainer!!.close()
    }
}
