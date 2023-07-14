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
import io.micronaut.guides.feature.template.openapiServerGradle;
import io.micronaut.starter.build.dependencies.PomDependencyVersionResolver;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Singleton
public class OpenApiServerGeneratorFeature extends AbstractOpenApiGeneratorFeature {
    public OpenApiServerGeneratorFeature(PomDependencyVersionResolver resolver) {
        super(resolver);
    }

    @Override
    protected String getSimpleName() {
        return "server";
    }

    @Override
    protected String getKind() {
        return "server";
    }

    @Override
    protected String getDefinitionFile() {
        return "src/main/resources/library-definition.yml";
    }

    @Override
    protected String getPackageName() {
        return "example.micronaut";
    }

    @Override
    protected RockerModel provideGradleModel() {
        return openapiServerGradle.template(getKind(), getDefinitionFile(), getPackageName() + ".api", getPackageName() + ".model", getPackageName() + ".invoker");
    }

    @Override
    protected Map<String, String> provideMavenProperties() {
        var allProperties = new LinkedHashMap<>(super.provideMavenProperties());
        allProperties.putAll(Map.of(
                "micronaut.openapi.use.reactive", "false",
                "micronaut.openapi.server.use.auth", "true"
        ));
        return Collections.unmodifiableMap(allProperties);
    }
}
