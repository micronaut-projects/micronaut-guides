/*
 * Copyright 2003-2021 the original author or authors.
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
package io.micronaut.guides.feature.openapi;

import io.micronaut.guides.feature.template.openapiGradle;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.PomDependencyVersionResolver;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.template.RockerWritable;

import java.util.Map;

public abstract class AbstractOpenApiGeneratorFeature implements Feature {
    private final PomDependencyVersionResolver resolver;

    public AbstractOpenApiGeneratorFeature(PomDependencyVersionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public String getName() {
        return "openapi-generator-" + getSimpleName();
    }

    protected abstract String getSimpleName();

    protected abstract String getKind();

    protected abstract String getDefinitionFile();

    protected abstract String getPackageName();

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .id("io.micronaut.openapi")
                    .lookupArtifactId("micronaut-gradle-plugin")
                    .version(resolver.resolve("micronaut-gradle-plugin").get().getVersion())
                    .extension(new RockerWritable(openapiGradle.template(getKind(), getDefinitionFile(), getPackageName() + ".api", getPackageName() + ".model")))
                    .build());
        } else {
            // temporary fix, guides currently depend on M4
            generatorContext.getBuildProperties().put("micronaut-maven-plugin.version", "4.0.0-M5");
            generatorContext.getBuildProperties().putAll(Map.of(
                    "micronaut.openapi.generate." + getKind(), "true",
                    "micronaut.openapi.definition", getDefinitionFile(),
                    "micronaut.openapi.api.package.name", getPackageName() + ".api",
                    "micronaut.openapi.model.package.name", getPackageName() + ".model"
            ));
        }
    }
}
