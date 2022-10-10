/*
 * Copyright 2017-2022 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.guides.feature.springboot.replacements;

import com.fizzed.rocker.RockerModel;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.guides.feature.springboot.template.help;
import io.micronaut.guides.feature.springboot.template.sourceCompatibility;
import io.micronaut.guides.feature.springboot.template.springBootBuildGradle;
import io.micronaut.guides.feature.springboot.template.springBootGitignore;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.gradle.GradleBuild;
import io.micronaut.starter.build.gradle.GradleBuildCreator;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.build.KotlinBuildPlugins;
import io.micronaut.starter.feature.build.MicronautBuildPlugin;
import io.micronaut.starter.feature.lang.LanguageFeature;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.Template;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static io.micronaut.guides.feature.springboot.SpringBootApplicationFeature.*;

@Singleton
@Replaces(io.micronaut.starter.feature.build.gradle.Gradle.class)
public class Gradle extends io.micronaut.starter.feature.build.gradle.Gradle {

    public Gradle(GradleBuildCreator dependencyResolver,
                  MicronautBuildPlugin micronautBuildPlugin,
                  KotlinBuildPlugins kotlinBuildPlugins) {
        super(dependencyResolver, micronautBuildPlugin, kotlinBuildPlugins);
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!isSpringBootApplication(featureContext)) {
            super.processSelectedFeatures(featureContext);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        if (isSpringBootApplication(generatorContext)) {
            generatorContext.addTemplate("help", new RockerTemplate(Template.ROOT, "HELP.md", help.template()));
        }
    }

    @Override
    protected RockerModel buildFile(GeneratorContext generatorContext, GradleBuild build) {
        if (isSpringBootApplication(generatorContext)) {
            return springBootBuildGradle.template(generatorContext.getProject(), build, generatorContext.getFeatures());
        }
        return super.buildFile(generatorContext, build);
    }

    @Override
    protected RockerModel gitignore(GeneratorContext generatorContext) {
        if (isSpringBootApplication(generatorContext)) {
            return springBootGitignore.template();
        }
        return super.gitignore(generatorContext);
    }

    @Override
    protected void addGradleProperties(GeneratorContext generatorContext) {
        if (!isSpringBootApplication(generatorContext)) {
            super.addGradleProperties(generatorContext);
        }
    }

    @Override
    protected List<GradlePlugin> extraPlugins(GeneratorContext generatorContext) {
        if (isSpringBootApplication(generatorContext)) {
            return gradlePluginIdForLanguage(generatorContext.getFeatures().language())
                    .map(pluginId -> Collections.singletonList(
                            GradlePlugin.builder()
                                    .id(pluginId)
                                    .extension(new RockerTemplate(sourceCompatibility.template(generatorContext.getFeatures().getTargetJdk())))
                                        .build())
                    )
                    .orElseGet(Collections::emptyList);
        }
        return super.extraPlugins(generatorContext);
    }
    protected Optional<String> gradlePluginIdForLanguage(LanguageFeature language) {
        if (language.isGroovy()) {
            return Optional.of("groovy");
        } else if (language.isJava()) {
            return Optional.of("java");
        }
        return Optional.empty();
    }
}

