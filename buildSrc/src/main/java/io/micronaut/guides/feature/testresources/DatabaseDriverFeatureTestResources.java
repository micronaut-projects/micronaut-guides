package io.micronaut.guides.feature.testresources;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.OneOfFeature;
import io.micronaut.starter.feature.database.jdbc.JdbcFeature;
import io.micronaut.starter.feature.database.r2dbc.R2dbcFeature;

import java.util.Collections;
import java.util.Map;

public abstract class DatabaseDriverFeatureTestResources extends TestResourcesFeature implements OneOfFeature {

    private final JdbcFeature jdbcFeature;

    public DatabaseDriverFeatureTestResources() {
        this(null, null);
    }

    public DatabaseDriverFeatureTestResources(JdbcFeature jdbcFeature, TestResources testResources) {
        super(testResources);
        this.jdbcFeature = jdbcFeature;
    }

    @Override
    public Class<?> getFeatureClass() {
        return io.micronaut.starter.feature.database.DatabaseDriverFeature.class;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        super.processSelectedFeatures(featureContext);
        if (!(featureContext.isPresent(JdbcFeature.class) || featureContext.isPresent(R2dbcFeature.class))
                && jdbcFeature != null) {
            featureContext.addFeature(jdbcFeature);
        }
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }

    public abstract boolean embedded();

    public String getJdbcUrl() {
        return null;
    }

    public String getR2dbcUrl() {
        return null;
    }

    public abstract String getDriverClass();

    public String getDefaultUser() {
        return null;
    }

    public String getDefaultPassword() {
        return null;
    }

    public abstract String getDataDialect();

    public Map<String, Object> getAdditionalConfig() {
        return Collections.emptyMap();
    }

}

