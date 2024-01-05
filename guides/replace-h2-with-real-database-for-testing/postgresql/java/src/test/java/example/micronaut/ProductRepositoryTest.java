/*
 * Copyright 2017-2024 original authors
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
package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.test.annotation.Sql;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false) // <1>
@Testcontainers(disabledWithoutDocker = true) // <2>
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // <3>
@Sql(scripts = "classpath:sql/seed-data.sql", phase = Sql.Phase.BEFORE_EACH) // <4>
class ProductRepositoryTest implements TestPropertyProvider {  // <5>

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15.2-alpine"
    ).withCopyFileToContainer(MountableFile.forClasspathResource("sql/init-db.sql"), "/docker-entrypoint-initdb.d/init-db.sql");

    @Override
    public @NonNull Map<String, String> getProperties() { // <5>
        if (!postgres.isRunning()) {
            postgres.start();
        }
        return Map.of("datasources.default.driver-class-name", "org.postgresql.Driver",
                "datasources.default.url", postgres.getJdbcUrl(),
                "datasources.default.username", postgres.getUsername(),
                "datasources.default.password", postgres.getPassword());
    }

    @Inject
    Connection connection;

    @Inject
    ResourceLoader resourceLoader;

    @Inject
    ProductRepository productRepository;

    @Test
    void shouldGetAllProducts() {
        List<Product> products = productRepository.findAll();
        assertEquals(2, products.size());
    }

    @Test
    void shouldNotCreateAProductWithDuplicateCode() {
        Product product = new Product(3L, "p101", "Test Product");
        productRepository.createProductIfNotExists(product);
        Optional<Product> optionalProduct = productRepository.findById(product.getId());
        assertTrue(optionalProduct.isEmpty());
    }
}
