package io.micronaut.guides.feature.testresources;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.Priority;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.database.Data;
import io.micronaut.starter.feature.database.DataFeature;
import io.micronaut.starter.feature.database.TestContainers;
import io.micronaut.starter.feature.database.TestContainersFeature;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;

/**
 * Base class for our data-mongo features.
 */
public abstract class DataMongoFeature extends TestResourcesFeature implements DataFeature {

    private static final String MONGODB_GROUP = "org.mongodb";
    private static final String MICRONAUT_DATA_GROUP = "io.micronaut.data";
    private static final String MICRONAUT_DATA_VERSION = "micronaut.data.version";
    private static final String MICRONAUT_DATA_PROCESSOR_ARTIFACT = "micronaut-data-processor";
    private static final String MICRONAUT_DATA_DOCUMENT_PROCESSOR_ARTIFACT = "micronaut-data-document-processor";
    private static final String MICRONAUT_DATA_MONGODB_ARTIFACT = "micronaut-data-mongodb";
    private static final String MONGODB_URI_CONFIGURATION_KEY = "mongodb.uri";
    private static final String MONGODB_URI_CONFIGURATION_VALUE = "mongodb://${MONGO_HOST:localhost}:${MONGO_PORT:27017}/mydb";

    private final Data data;

    protected DataMongoFeature(Data data, TestResources testResources) {
        super(testResources);
        this.data = data;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put(MONGODB_URI_CONFIGURATION_KEY, MONGODB_URI_CONFIGURATION_VALUE);

        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            generatorContext.addDependency(Dependency.builder()
                    .order(Priority.MICRONAUT_DATA_PROCESSOR.getOrder())
                    .annotationProcessor(true)
                    .groupId(MICRONAUT_DATA_GROUP)
                    .artifactId(MICRONAUT_DATA_PROCESSOR_ARTIFACT)
                    .versionProperty(MICRONAUT_DATA_VERSION));
        }
        generatorContext.addDependency(Dependency.builder()
                .annotationProcessor()
                .groupId(MICRONAUT_DATA_GROUP)
                .artifactId(MICRONAUT_DATA_DOCUMENT_PROCESSOR_ARTIFACT)
                .versionProperty(MICRONAUT_DATA_VERSION));
        generatorContext.addDependency(Dependency.builder()
                .compile()
                .groupId(MICRONAUT_DATA_GROUP)
                .artifactId(MICRONAUT_DATA_MONGODB_ARTIFACT)
                .versionProperty(MICRONAUT_DATA_VERSION));

        Dependency.Builder driverDependency = Dependency.builder()
                .groupId(MONGODB_GROUP)
                .artifactId(mongoArtifact());
        // Needs to be an implementation dependency for the Groovy compiler
        driverDependency = generatorContext.getLanguage() == Language.GROOVY ? driverDependency.compile() : driverDependency.runtime();
        generatorContext.addDependency(driverDependency);
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-data/latest/guide/#mongo";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        super.processSelectedFeatures(featureContext);
        featureContext.addFeature(data);
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.mongodb.com";
    }

    @NonNull
    protected abstract String mongoArtifact();
}
