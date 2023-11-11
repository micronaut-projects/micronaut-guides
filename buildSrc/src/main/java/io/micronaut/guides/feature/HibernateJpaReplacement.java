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
package io.micronaut.guides.feature;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.database.jdbc.JdbcFeature;
import io.micronaut.starter.feature.migration.MigrationFeature;
import io.micronaut.starter.feature.database.*;
import jakarta.inject.Singleton;

@Replaces(HibernateJpa.class)
@Singleton
public class HibernateJpaReplacement extends HibernateJpa {

    private static final String ARTIFACT_ID_MICRONAUT_DATA_TX_HIBERNATE = "micronaut-data-tx-hibernate";
    private static final String ARTIFACT_ID_MICRONAUT_HIBERNATE_JPA = "micronaut-hibernate-jpa";
    private static final String ARTIFACT_ID_MICRONAUT_DATA_MODEL = "micronaut-data-model";

    public HibernateJpaReplacement(JdbcFeature jdbcFeature) {
        super(jdbcFeature);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put(JPA_HIBERNATE_PROPERTIES_HBM2DDL,
                generatorContext.getFeatures().hasFeature(MigrationFeature.class) ? Hbm2ddlAuto.NONE.toString() :
                        Hbm2ddlAuto.UPDATE.toString());
        addDependencies(generatorContext);
    }

    protected void addDependencies(GeneratorContext generatorContext) {
        generatorContext.addDependency(MicronautDependencyUtils.sqlDependency()
                .artifactId(ARTIFACT_ID_MICRONAUT_HIBERNATE_JPA)
                .compile());
        generatorContext.addDependency(MicronautDependencyUtils.dataDependency()
                .artifactId(ARTIFACT_ID_MICRONAUT_DATA_TX_HIBERNATE)
                .compile());
        generatorContext.addDependency(MicronautDependencyUtils.dataDependency()
                .artifactId(ARTIFACT_ID_MICRONAUT_DATA_MODEL)
                .compile());
    }
}
