package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

@MicronautTest // <1>
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // <2>
public class BaseMysqlTest implements TestPropertyProvider { // <3>

    static GenericContainer<?> mysqlContainer;

    @Inject
    EmbeddedApplication<?> application;

    @Inject
    @Client("/")
    HttpClient httpClient; // <4>

    void startMySQL() {
        if (mysqlContainer == null) {
            mysqlContainer = new GenericContainer<>(DockerImageName.parse("mysql:oracle"))
                    .withExposedPorts(3306)
                    .withEnv("MYSQL_ROOT_PASSWORD", "pass")
                    .waitingFor(Wait.forLogMessage(".*/usr/sbin/mysqld: ready for connections.*\\n", 2));
        }
        if (!mysqlContainer.isRunning()) {
            mysqlContainer.start();
        }
    }

    String getMySQLDbUri() {
        if (mysqlContainer == null || !mysqlContainer.isRunning()) {
            startMySQL();
        }
        return "jdbc:mysql://localhost:" + mysqlContainer.getMappedPort(3306) + "/mysql";
    }

    @Override
    @NonNull
    public Map<String, String> getProperties() { // <5>
        return Collections.singletonMap("jpa.default.properties.hibernate.connection.url", getMySQLDbUri());
    }

    @AfterAll
    public static void stop() {
        mysqlContainer.close();
    }
}
