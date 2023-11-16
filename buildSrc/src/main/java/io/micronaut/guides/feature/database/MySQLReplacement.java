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
package io.micronaut.guides.feature.database;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.database.MySQL;
import io.micronaut.starter.feature.database.TestContainers;
import io.micronaut.starter.feature.database.jdbc.JdbcFeature;
import io.micronaut.starter.feature.testresources.TestResources;
import jakarta.inject.Singleton;

import java.util.Optional;

/**
 * Replacement to use the old MySQL dependency.
 *
 * https://github.com/micronaut-projects/micronaut-starter/pull/2064 breaks 4.2.0 maven-plugin/test-resources.
 *
 * The maven plugin currently uses an old test-resources version, which doesn't recognise the new dependency as mysql
 */
@Singleton
@Replaces(MySQL.class)
public class MySQLReplacement extends MySQL {

    private static final Dependency.Builder DEPENDENCY_MYSQL_CONNECTOR_JAVA = Dependency.builder()
            .groupId("mysql")
            .artifactId("mysql-connector-java")
            .runtime()
            .template();

    public MySQLReplacement(JdbcFeature jdbcFeature,
                 TestContainers testContainers,
                 TestResources testResources) {
        super(jdbcFeature, testContainers, testResources);
    }

    @Override
    @NonNull
    public Optional<Dependency.Builder> getJavaClientDependency() {
        return Optional.of(DEPENDENCY_MYSQL_CONNECTOR_JAVA);
    }
}
