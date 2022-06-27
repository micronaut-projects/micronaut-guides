package io.micronaut.guides.feature.testresources;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.messaging.MessagingFeature;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
public class KafkaTestResources extends TestResourcesFeature implements DefaultFeature, MessagingFeature {

    public static final String NAME = "kafka-testresources";

    public KafkaTestResources(TestResources testResources) {
        super(testResources);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Kafka Messaging";
    }

    @Override
    public String getDescription() {
        return "Adds support for Kafka messaging";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("kafka.bootstrap.servers", "localhost:9092");
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.kafka")
                .artifactId("micronaut-kafka")
                .compile());
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return applicationType == ApplicationType.MESSAGING &&
                selectedFeatures.stream().noneMatch(feature -> feature instanceof MessagingFeature);
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-kafka/latest/guide/index.html";
    }
}
