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
package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.core.io.ResourceLoader
import io.micronaut.test.annotation.Sql
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.spock.Testcontainers
import org.testcontainers.utility.MountableFile
import spock.lang.Shared
import spock.lang.Specification

import java.sql.Connection

@MicronautTest(startApplication = false) // <1>
@Testcontainers(disabledWithoutDocker = true) // <2>
@Sql(scripts = 'classpath:sql/seed-data.sql', phase = Sql.Phase.BEFORE_EACH) // <4>
class ProductRepositorySpec extends Specification implements TestPropertyProvider { // <5>

    @Shared // <3>
    PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            'postgres:15.2-alpine'
    ).withCopyFileToContainer(MountableFile.forClasspathResource('sql/init-db.sql'), '/docker-entrypoint-initdb.d/init-db.sql')

    @Override
    @NonNull
    Map<String, String> getProperties() { // <5>
        if (!postgres.isRunning()) {
            postgres.start()
        }
        [
                'datasources.default.driver-class-name': 'org.postgresql.Driver',
                'datasources.default.url'              : postgres.jdbcUrl,
                'datasources.default.username'         : postgres.username,
                'datasources.default.password'         : postgres.password
        ]
    }

    @Inject
    Connection connection

    @Inject
    ResourceLoader resourceLoader

    @Inject
    ProductRepository productRepository

    void 'should get all products'() {
        when:
        List<Product> products = productRepository.findAll()

        then:
        products.size() == 2
    }

    void 'should not create a product with duplicate code'() {
        when:
        Product product = new Product(3L, 'p101', 'Test Product')
        productRepository.createProductIfNotExists(product)
        Optional<Product> optionalProduct = productRepository.findById(product.id)

        then:
        optionalProduct.isEmpty()
    }
}
