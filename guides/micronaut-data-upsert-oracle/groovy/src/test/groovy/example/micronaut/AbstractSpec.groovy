package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.test.support.TestPropertyProvider
import org.testcontainers.oracle.OracleContainer
import org.testcontainers.utility.DockerImageName
import spock.lang.Specification

abstract class AbstractSpec extends Specification implements TestPropertyProvider { // <1>

    public static final String IMAGE_NAME = 'gvenzl/oracle-free:23.26.1-faststart'

    static OracleContainer container = new OracleContainer(DockerImageName.parse(IMAGE_NAME)) // <2>

    @Override
    @NonNull
    Map<String, String> getProperties() {
        if (!container.running) {
            container.start() // <3>
        }
        [ // <4>
                'datasources.default.driver-class-name': 'oracle.jdbc.OracleDriver',
                'datasources.default.url'              : container.jdbcUrl,
                'datasources.default.username'         : container.username,
                'datasources.default.password'         : container.password,
        ]
    }
}
