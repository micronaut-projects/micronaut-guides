package io.micronaut.guides.feature.springboot;
import io.micronaut.guides.feature.springboot.template.kotlinCompile;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Coordinate;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.gradle.GradleDsl;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.ApplicationFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.lang.kotlin.Kotlin;
import io.micronaut.starter.feature.lang.kotlin.KotlinApplicationFeature;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.Options;

import io.micronaut.starter.template.RockerTemplate;
import jakarta.inject.Singleton;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@Replaces(Kotlin.class)
@Singleton
public class SpringBootKotlin extends Kotlin {
    public static final String GROUP_JETBRAINS_KOTLIN = "org.jetbrains.kotlin";
    public static final Dependency DEPENDENCY_KOTLIN_REFLECT = Dependency.builder()
            .groupId(GROUP_JETBRAINS_KOTLIN)
            .artifactId("kotlin-reflect")
            .compile()
            .build();
    public static final Dependency DEPENDENCY_KOTLIN_STDLIB_JDK8 = Dependency.builder()
            .groupId(GROUP_JETBRAINS_KOTLIN)
            .artifactId("kotlin-stdlib-jdk8")
            .compile()
            .build();
    public static final GradlePlugin GRADLE_PLUGIN_SPRING = GradlePlugin.builder()
            .id("org.jetbrains.kotlin.plugin.spring")
            .lookupArtifactId("kotlin-allopen")
            .build();
    public static final GradlePlugin.Builder GRADLE_PLUGIN_KOTLIN_JVM = GradlePlugin.builder()
            .id("org.jetbrains.kotlin.jvm")
            .lookupArtifactId("kotlin-gradle-plugin");

    public SpringBootKotlin(List<KotlinApplicationFeature> applicationFeatures) {
        super(applicationFeatures);
    }

    @Override
    @NonNull
    public String getName() {
        return "spring-boot-kotlin";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (SpringBootApplicationFeature.isSpringBootApplication(featureContext)) {
            processSelectedFeatures(featureContext, SpringBootApplicationFeature.class::isInstance);
        } else {
            processSelectedFeatures(featureContext, f -> !(f instanceof SpringBootApplicationFeature));
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (SpringBootApplicationFeature.isSpringBootApplication(generatorContext)) {
            if (generatorContext.getBuildTool().isGradle()) {
                generatorContext.addBuildPlugin(GRADLE_PLUGIN_SPRING);
                generatorContext.addBuildPlugin(GRADLE_PLUGIN_KOTLIN_JVM
                        .extension(new RockerTemplate(kotlinCompile.template(generatorContext.getBuildTool().getGradleDsl().orElse(GradleDsl.GROOVY), generatorContext.getFeatures().getTargetJdk())))
                        .build());
            }
        } else {
            addKotlinVersionProperty(generatorContext);
        }
        addDependencies(generatorContext);
    }

    protected void addDependencies(GeneratorContext generatorContext) {
        if (SpringBootApplicationFeature.isSpringBootApplication(generatorContext)) {
            generatorContext.addDependency(DEPENDENCY_KOTLIN_REFLECT);
            generatorContext.addDependency(DEPENDENCY_KOTLIN_STDLIB_JDK8);
        } else {
            super.addDependencies(generatorContext);
        }
    }
}
