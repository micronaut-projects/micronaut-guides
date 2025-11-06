/*
 * Copyright 2017-2023 original authors
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
import io.micronaut.starter.application.generator.GeneratorContext;
import jakarta.inject.Singleton;
import io.micronaut.starter.build.dependencies.Dependency;
import static io.spring.start.SpringBootDependencies.GROUP_ID_ORG_SPRINGFRAMEWORK_BOOT;

@Singleton
public class SpringBootStarterThymeleaf implements SpringBootStarterFeature {
    public static final String NAME = "spring-boot-starter-thymeleaf";
    public static final String ARTIFACT_ID_SPRING_BOOT_STARTER_THYMELEAF = "spring-boot-starter-thymeleaf";

    public static final Dependency.Builder DEPENDENCY_SPRINGBOOT_STARTER_THYMELEAF = Dependency.builder()
            .groupId(GROUP_ID_ORG_SPRINGFRAMEWORK_BOOT)
            .artifactId(ARTIFACT_ID_SPRING_BOOT_STARTER_THYMELEAF)
            .compile();

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Spring Starter Thymeleaf";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Starter for building MVC web applications using Thymeleaf views.";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_SPRINGBOOT_STARTER_THYMELEAF);
    }

}
