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

import static io.spring.start.SpringBootDependencies.*;

@Singleton
public class SpringBootStarterJooq implements SpringBootStarterFeature {
    public static final String NAME = "spring-boot-starter-jooq";

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "JOOQ Access Layer";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Generate Java code from your database and build type safe SQL queries through a fluent API.";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_SPRINGBOOT_STARTER_JOOQ);
        generatorContext.addDependency(DEPENDENCY_SPRINGBOOT_STARTER_TEST);
    }

}
