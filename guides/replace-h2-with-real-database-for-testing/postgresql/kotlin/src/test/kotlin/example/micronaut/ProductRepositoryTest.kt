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
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.MountableFile
import java.sql.Connection

@MicronautTest(startApplication = false) // <1>
@Testcontainers(disabledWithoutDocker = true) // <2>
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // <3>
@Sql(scripts = ["classpath:sql/seed-data.sql"], phase = Sql.Phase.BEFORE_EACH) // <4>
class ProductRepositoryTest : TestPropertyProvider { // <5>

    companion object {
        @Container
        @JvmField
        val postgres: PostgreSQLContainer<*> = PostgreSQLContainer<Nothing>("postgres:15.2-alpine").apply {
            withCopyFileToContainer(
                MountableFile.forClasspathResource("sql/init-db.sql"),
                "/docker-entrypoint-initdb.d/init-db.sql"
            )
        }
    }

    @NonNull
    override fun getProperties(): Map<String, String> { // <5>
        if (!postgres.isRunning) {
            postgres.start()
        }
        return mapOf(
            "datasources.default.driver-class-name" to "org.postgresql.Driver",
            "datasources.default.url" to postgres.jdbcUrl,
            "datasources.default.username" to postgres.username,
            "datasources.default.password" to postgres.password
        )
    }

    @Inject
    lateinit var connection: Connection

    @Inject
    lateinit var resourceLoader: ResourceLoader

    @Inject
    lateinit var productRepository: ProductRepository

    @Test
    fun shouldGetAllProducts() {
        val products = productRepository.findAll().toList()
        assertEquals(2, products.size)
    }

    @Test
    fun shouldNotCreateAProductWithDuplicateCode() {
        val product = Product(3L, "p101", "Test Product")
        productRepository.createProductIfNotExists(product)
        val optionalProduct = productRepository.findById(product.id)
        assertTrue(optionalProduct.isEmpty)
    }
}
