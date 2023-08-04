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
import io.micronaut.guides.feature.template.micronautMavenPlugin;
import io.micronaut.guides.feature.template.openapiServerGradle;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.PomDependencyVersionResolver;
import io.micronaut.starter.build.maven.MavenPlugin;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.template.RockerWritable;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class OpenApiServerGeneratorCustomizationFeature extends AbstractOpenApiGeneratorFeature {
    public OpenApiServerGeneratorCustomizationFeature(PomDependencyVersionResolver resolver) {
        super(resolver);
    }

    @Override
    protected String getSimpleName() {
        return "server-customization";
    }

    @Override
    protected String getKind() {
        return "server";
    }

    @Override
    protected String getDefinitionFile() {
        return "src/openapi/spec.yml";
    }

    @Override
    protected String getPackageName() {
        return "example.micronaut";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        generatorContext.addDependency(Dependency.builder().compile()
                .groupId("jakarta.persistence").artifactId("jakarta.persistence-api").version("3.1.0"));
        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            RockerModel mavenPluginModel = micronautMavenPlugin.template(
                    getParameterMappings(),
                    getResponseBodyMappings()
            );
            generatorContext.addBuildPlugin(MavenPlugin.builder()
                    .artifactId("micronaut-maven-plugin")
                    .extension(new RockerWritable(mavenPluginModel))
                    .build());
        }
    }

    @Override
    protected RockerModel provideGradleModel() {
        return openapiServerGradle.template(
                getKind(),
                getDefinitionFile(),
                getPackageName() + ".api",
                getPackageName() + ".model",
                getPackageName(),
                getParameterMappings(),
                getResponseBodyMappings()
        );
    }

    private List<Map<String, String>> getParameterMappings() {
        return List.of(
                Map.of("name", "mealName", "location", "QUERY", "mappedType", "example.micronaut.filter.RecipeFilter", "mappedName", "filter"),
                Map.of("name", "ingredientName", "location", "QUERY", "mappedType", "example.micronaut.filter.RecipeFilter", "mappedName", "filter"),
                Map.of("name", "page", "location", "QUERY", "mappedType", "io.micronaut.data.model.Pageable"),
                Map.of("name", "size", "location", "QUERY", "mappedType", "io.micronaut.data.model.Pageable"),
                Map.of("name", "sortOrder", "location", "QUERY", "mappedType", "io.micronaut.data.model.Pageable")
        );
    }

    private List<Map<String, String>> getResponseBodyMappings() {
        return List.of(
                Map.of("headerName", "Date", "mappedBodyType", "example.micronaut.dated.DatedResponse"),
                Map.of("headerName", "Last-Modified", "mappedBodyType", "example.micronaut.dated.DatedResponse"),
                Map.of("headerName", "X-Page-Number", "mappedBodyType", "io.micronaut.data.model.Page", "isListWrapper", "true"),
                Map.of("headerName", "X-Page-Count", "mappedBodyType", "io.micronaut.data.model.Page", "isListWrapper", "true"),
                Map.of("headerName", "X-Total-Count", "mappedBodyType", "io.micronaut.data.model.Page", "isListWrapper", "true"),
                Map.of("headerName", "X-Page-Size", "mappedBodyType", "io.micronaut.data.model.Page", "isListWrapper", "true")
        );
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
