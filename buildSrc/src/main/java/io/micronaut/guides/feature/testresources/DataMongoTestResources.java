package io.micronaut.guides.feature.testresources;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.database.DataMongo;
import jakarta.inject.Singleton;

@Singleton
public class DataMongoTestResources extends TestResourcesFeature {

    private final DataMongo dataMongo;

    public DataMongoTestResources(DataMongo dataMongo, TestResources testResources) {
        super(testResources);
        this.dataMongo = dataMongo;
    }

    @NonNull
    @Override
    public String getName() {
        return "data-mongodb-testresources";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        dataMongo.apply(generatorContext);
        super.apply(generatorContext);
    }
}
