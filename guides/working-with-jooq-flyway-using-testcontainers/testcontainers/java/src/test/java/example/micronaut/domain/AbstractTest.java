package example.micronaut.domain;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.test.support.TestPropertyProvider;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.Map;

public abstract class AbstractTest implements TestPropertyProvider { // <1>

    @Container // <2>
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.3-alpine");

    @Override
    public @NonNull Map<String, String> getProperties() { // <1>
        if (!postgres.isRunning()) {
            postgres.start();
        }
        return Map.of("datasources.default.url", postgres.getJdbcUrl(),
                "datasources.default.username", postgres.getUsername(),
                "datasources.default.password", postgres.getPassword());
    }
}
