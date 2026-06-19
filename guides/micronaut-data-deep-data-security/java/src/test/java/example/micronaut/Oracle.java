package example.micronaut;

import java.util.Map;
import org.testcontainers.oracle.OracleContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

public class Oracle {
    public static final String IMAGE_NAME = "gvenzl/oracle-free:23.26.1-faststart";
    // The faststart image already has a database, so startup scripts are the
    // reliable hook for schema creation.
    private static final String INIT_SCRIPT = "/container-entrypoint-startdb.d/01-hr-schema.sql";
    private static volatile OracleContainer container;

    public static Map<String, String> getProperties() {
        OracleContainer current = container;
        if (current == null) {
            synchronized (Oracle.class) {
                current = container;
                if (current == null) {
                    current = new OracleContainer(DockerImageName.parse(IMAGE_NAME))
                        .withCopyFileToContainer(MountableFile.forClasspathResource("oracle-init.sql"), INIT_SCRIPT);
                    current.start();
                    Runtime.getRuntime().addShutdownHook(new Thread(current::stop));
                    container = current;
                }
            }
        }
        return getProperties(current);
    }

    private static Map<String, String> getProperties(OracleContainer container) {
        return Map.of(
            "datasources.default.url", container.getJdbcUrl(),
            "datasources.default.username", container.getUsername(),
            "datasources.default.password", container.getPassword()
        );
    }

}
