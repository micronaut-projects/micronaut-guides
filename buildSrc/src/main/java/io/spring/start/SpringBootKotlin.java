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
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.lang.LanguageFeature;
import io.spring.start.templates.applicationkotlin;
import io.spring.start.templates.applicationtestkotlinjunit;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.RockerTemplate;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import java.util.Set;

@Named(SpringBootFramework.FRAMEWORK_SPRING_BOOT)
@Singleton
public class SpringBootKotlin implements LanguageFeature {

    protected final KotlinStdlib kotlinStdlib;
    protected final KotlinMavenPlugin kotlinMavenPlugin;
    protected final KotlinGradlePlugin kotlinGradlePlugin;
    protected final KotlinSpringGradlePlugin kotlinSpringGradlePlugin;
    protected final KotlinJackson kotlinJackson;
    protected final KotlinReflect kotlinReflect;

    public SpringBootKotlin(KotlinStdlib kotlinStdlib, KotlinMavenPlugin kotlinMavenPlugin, KotlinGradlePlugin kotlinGradlePlugin, KotlinSpringGradlePlugin kotlinSpringGradlePlugin, KotlinJackson kotlinJackson, KotlinReflect kotlinReflect) {
        this.kotlinStdlib = kotlinStdlib;
        this.kotlinMavenPlugin = kotlinMavenPlugin;
        this.kotlinGradlePlugin = kotlinGradlePlugin;
        this.kotlinSpringGradlePlugin = kotlinSpringGradlePlugin;
        this.kotlinJackson = kotlinJackson;
        this.kotlinReflect = kotlinReflect;
    }

    @Override
    @NonNull
    public String getName() {
        return "springboot-kotlin";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.getBuildTool().isGradle()) {
            featureContext.addFeature(kotlinGradlePlugin);
            featureContext.addFeature(kotlinSpringGradlePlugin);
        } else {
            featureContext.addFeature(kotlinStdlib);
            featureContext.addFeature(kotlinMavenPlugin);
        }
        featureContext.addFeature(kotlinReflect);
        featureContext.addFeature(kotlinJackson);
    }

    @Override
    public boolean isKotlin() {
        return true;
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return options.getLanguage() == Language.KOTLIN;
    }

    @Override
    public String getTargetFramework() {
        return SpringBootFramework.FRAMEWORK_SPRING_BOOT;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addApplication(generatorContext);
        if (generatorContext.getTestFramework() == TestFramework.JUNIT) {
            addApplicationTest(generatorContext);
        }
    }

    protected void addApplication(GeneratorContext generatorContext) {
        String sourcePath = generatorContext.getSourcePath("/{packagePath}/Application");
        generatorContext.addTemplate("Application",
                new RockerTemplate(sourcePath, applicationkotlin.template(generatorContext.getProject())));
    }

    protected void addApplicationTest(GeneratorContext generatorContext) {
        String testSourcePath = generatorContext.getTestSourcePath("/{packagePath}/Application");
        generatorContext.addTemplate("ApplicationTest",
                new RockerTemplate(testSourcePath, applicationtestkotlinjunit.template(generatorContext.getProject())));
    }
}
