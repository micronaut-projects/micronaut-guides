package example.micronaut.repository;

import org.testcontainers.containers.OracleContainer;

import java.util.Map;

public class Oracle {

    public static OracleContainer oracle = new OracleContainer("gvenzl/oracle-xe:21-slim-faststart")
            .withDatabaseName("testDB")
            .withUsername("testUser")
            .withPassword("testPassword");

    public static Map<String, Object> getConfiguration() {
        start();
        return Map.of("datasources.default.url", oracle.getJdbcUrl(),
                "datasources.default.username", oracle.getUsername(),
                "datasources.default.password", oracle.getPassword(),
                "datasources.default.driver-class-name", oracle.getDriverClassName());
    }
    public static void start() {
        if (!oracle.isRunning()) {
            oracle.start();
        }
    }

    public static void stop() {
        if (oracle.isRunning()) {
            oracle.stop();
        }
    }

    public static void close() {
        oracle.close();
    }

}
