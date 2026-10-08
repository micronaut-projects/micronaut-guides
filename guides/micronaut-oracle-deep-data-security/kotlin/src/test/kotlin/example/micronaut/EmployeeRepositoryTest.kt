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

import io.micronaut.security.annotation.RunAs
import io.micronaut.test.annotation.Sql
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.math.BigDecimal

@Sql("classpath:employees-test-data.sql") // <1>
@MicronautTest(startApplication = false) // <2>
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // <3>
@RunAs(name = "sergio") // <4>
class EmployeeRepositoryTest : TestPropertyProvider { // <5>

    @Inject
    lateinit var repository: EmployeeRepository

    override fun getProperties(): Map<String, String> = Oracle.getProperties()

    @Test
    fun findAll() {
        assertEquals(5, repository.findAll().size)
    }

    @Test
    fun getSalaryAggregates() {
        assertEquals(
            SalaryAggregates(BigDecimal("6900"), BigDecimal("13000"), BigDecimal("49130"), 5L),
            repository.getSalaryAggregates() // <6>
        )
    }
}
