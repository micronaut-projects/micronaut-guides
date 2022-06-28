package io.micronaut.guides.feature.testresources;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.messaging.MessagingFeature;
import jakarta.inject.Singleton;

@Singleton
public class RabbitMQ extends TestResourcesFeature implements MessagingFeature {

    public static final String NAME = "rabbitmq-testresources";

    public RabbitMQ(TestResources testResources) {
        super(testResources);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "RabbitMQ Messaging";
    }

    @Override
    public String getDescription() {
        return "Adds support for RabbitMQ messaging";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put ("rabbitmq.uri", "amqp://localhost:5672");
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.rabbitmq")
                .artifactId("micronaut-rabbitmq")
                .compile());
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-rabbitmq/latest/guide/index.html";
    }
}

