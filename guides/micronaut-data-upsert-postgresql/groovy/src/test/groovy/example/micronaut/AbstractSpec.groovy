package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.test.support.TestPropertyProvider
import org.testcontainers.containers.PostgreSQLContainer
import spock.lang.Specification

abstract class AbstractSpec extends Specification implements TestPropertyProvider { // <1>

    static PostgreSQLContainer postgres = new PostgreSQLContainer('postgres:15.3-alpine') // <2>

    @Override
    @NonNull
    Map<String, String> getProperties() {
        if (!postgres.running) {
            postgres.start() // <3>
        }
        [
                'datasources.default.driver-class-name': 'org.postgresql.Driver', // <4>
                'datasources.default.url'              : postgres.jdbcUrl,
                'datasources.default.username'         : postgres.username,
                'datasources.default.password'         : postgres.password,
        ]
    }
}
