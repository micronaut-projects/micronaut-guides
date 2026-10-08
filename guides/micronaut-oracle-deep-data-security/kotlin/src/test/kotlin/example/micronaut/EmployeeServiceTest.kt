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
import io.micronaut.security.authentication.Authentication
import io.micronaut.test.annotation.Sql
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.math.BigDecimal

@Sql("classpath:employees-test-data.sql")
@MicronautTest(startApplication = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RunAs(name = "emma@supremo.onmicrosoft.com")
class EmployeeServiceTest : TestPropertyProvider {

    @Inject
    lateinit var service: EmployeeService

    override fun getProperties(): Map<String, String> = Oracle.getProperties()

    @Test
    fun employeeModel() {
        val model = service.employeeModel(Authentication.build("emma@supremo.onmicrosoft.com"))
        val employees = assertInstanceOf(Collection::class.java, model["employees"])
        assertEquals(5, employees.size)
        assertEquals(
            SalarySummaryDto(BigDecimal("6900"), BigDecimal("13000"), BigDecimal("9826.00"), 5L),
            model["salarySummary"]
        )
    }

    @Test
    fun employeeModelIsEmptyForAnonymousUsers() {
        assertTrue(service.employeeModel(null).isEmpty())
    }
}
