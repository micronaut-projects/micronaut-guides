package io.micronaut.guides.feature;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.guides.feature.template.gebGradleChrome;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.template.RockerWritable;

import javax.inject.Singleton;

@Singleton
public class GebChrome extends Geb {

    @NonNull
    @Override
    public String getName() {
        return "geb-chrome";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);

        generatorContext.addDependency(Dependency.builder().groupId("org.seleniumhq.selenium").lookupArtifactId("selenium-chrome-driver").test().build());

        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .id("com.github.erdi.webdriver-binaries")
                    .lookupArtifactId("webdriver-binaries-gradle-plugin")
                    .extension(new RockerWritable(gebGradleChrome.template()))
                    .build());
        }
    }
}
