package io.micronaut.guides.feature.cloudsql;

import io.micronaut.guides.feature.AbstractFeature;
import jakarta.inject.Singleton;

@Singleton
public class CloudSqlMySQL extends AbstractFeature {

    public CloudSqlMySQL() {
        super("gcp-cloudsql-mysql", "mysql-socket-factory");
    }
}
