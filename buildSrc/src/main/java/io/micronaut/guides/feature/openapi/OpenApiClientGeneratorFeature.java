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

import com.fizzed.rocker.RockerModel;
import io.micronaut.guides.feature.template.openapiClientGradle;
import io.micronaut.starter.build.dependencies.PomDependencyVersionResolver;
import jakarta.inject.Singleton;

@Singleton
public class OpenApiClientGeneratorFeature extends AbstractOpenApiGeneratorFeature {
    public OpenApiClientGeneratorFeature(PomDependencyVersionResolver resolver) {
        super(resolver);
    }

    @Override
    protected String getSimpleName() {
        return "client";
    }

    @Override
    protected String getKind() {
        return "client";
    }

    @Override
    protected String getDefinitionFile() {
        return "src/openapi/openmeteo.yml";
    }

    @Override
    protected String getPackageName() {
        return "example.openmeteo";
    }

    @Override
    protected RockerModel provideGradleModel() {
        return openapiClientGradle.template(getKind(), getDefinitionFile(), getPackageName() + ".api", getPackageName() + ".model");
    }

}
