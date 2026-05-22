/*
 * Copyright 2017-2026 original authors
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
package io.spring.start;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.CoordinateResolver;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;

@Singleton
public class KotlinKaptGradlePlugin implements Feature {

    private final CoordinateResolver coordinateResolver;

    public KotlinKaptGradlePlugin(CoordinateResolver coordinateResolver) {
        this.coordinateResolver = coordinateResolver;
    }

    @Override
    public @NonNull String getName() {
        return "kotlin-kapt-gradle-plugin";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            coordinateResolver.resolve("kotlin-gradle-plugin").ifPresent(coordinate ->
                    generatorContext.addBuildPlugin(GradlePlugin.builder()
                            .id("org.jetbrains.kotlin.kapt")
                            .version(coordinate.getVersion())
                            .build()));
        }
    }
}
