/*
 * Copyright 2003-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.guides.feature;

import com.fizzed.rocker.RockerModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.guides.feature.template.snapshotGradleSettingsExtension;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.PomDependencyVersionResolver;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.template.RockerWritable;
import jakarta.inject.Singleton;
import io.micronaut.guides.feature.template.jsonSchemaGradle;

import java.util.Map;

@Singleton
public class JsonSchemaGenerator implements Feature {
    private final PomDependencyVersionResolver resolver;

    public JsonSchemaGenerator(PomDependencyVersionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public @NonNull String getName() {
        return "json-schema-generator";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    protected String getOutputPackageName() {
        return "com.example.openweather";
    }

    protected String getInputUrl() {
        return "https://json.schemastore.org/openweather.current.json";
    }

    protected RockerModel provideGradleModel() {
        return jsonSchemaGradle.template(getInputUrl(), getOutputPackageName());
    }

    protected Map<String, String> provideMavenProperties() {
        return Map.of(
                "micronaut.jsonschema.generator.enabled", "true",
                "micronaut.jsonschema.generator.input-url", getInputUrl(),
                "micronaut.jsonschema.generator.outputPackageName", getOutputPackageName()
        );
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .id("io.micronaut.jsonschema")
                    //.lookupArtifactId("micronaut-gradle-plugin")
                    //.version(resolver.resolve("micronaut-gradle-plugin").get().getVersion())
                    .version("4.5.0-SNAPSHOT")
                    .extension(new RockerWritable(provideGradleModel()))
                    .settingsExtension(new RockerWritable(snapshotGradleSettingsExtension.template()))
                    .build());
        } else {
            generatorContext.getBuildProperties().putAll(provideMavenProperties());
            generatorContext.getBuildProperties().put("micronaut-maven-plugin.version", "4.5.0-SNAPSHOT");
        }
    }
}
