package examplemicronaut;

import io.micronaut.configuration.jdbc.hikari.DatasourceConfiguration;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.BeanCreatedEvent;
import io.micronaut.context.event.BeanCreatedEventListener;

import jakarta.inject.Singleton;

@Singleton // <1>
@Requires(env = Environment.GOOGLE_COMPUTE)
@Requires(property = GoogleCloudSqlSetup.CLOUD_SQL_CONNECTION_NAME)
public class GoogleCloudSqlSetup implements BeanCreatedEventListener<DatasourceConfiguration> {
    public static final String CLOUD_SQL_CONNECTION_NAME = "cloud.sql.connection.name";
    private static final String DB_NAME = System.getenv("DB_NAME");
    private final String cloudSqlInstance;

    public GoogleCloudSqlSetup(@Property(name = CLOUD_SQL_CONNECTION_NAME) String cloudSqlInstance) {
        this.cloudSqlInstance = cloudSqlInstance;
    }

    @Override
    public DatasourceConfiguration onCreated(BeanCreatedEvent<DatasourceConfiguration> event) {
        DatasourceConfiguration config = event.getBean();
        if (DB_NAME != null) {
            config.setJdbcUrl(String.format("jdbc:mysql:///%s", DB_NAME));
        }
        config.addDataSourceProperty("socketFactory", "com.google.cloud.sql.mysql.SocketFactory");
        config.addDataSourceProperty("cloudSqlInstance", cloudSqlInstance);
        return config;
    }
}