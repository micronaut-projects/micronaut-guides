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
import io.micronaut.security.authentication.Authentication
import io.micronaut.test.annotation.Sql
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import spock.lang.Specification

@Sql('classpath:employees-test-data.sql')
@MicronautTest(startApplication = false)
class EmployeeServiceSpec extends Specification implements TestPropertyProvider {

    @Inject
    EmployeeService service

    @Inject
    RunAsUser runAsUser

    @Override
    @NonNull
    Map<String, String> getProperties() {
        Oracle.getProperties()
    }

    void 'employee model contains the employees and the salary summary'() {
        when:
        Map<String, Object> model = runAsUser.emma { service.employeeModel(Authentication.build('emma@supremo.onmicrosoft.com')) }

        then:
        model.employees instanceof Collection
        (model.employees as Collection).size() == 5
        model.salarySummary == new SalarySummaryDto(new BigDecimal('6900'), new BigDecimal('13000'), new BigDecimal('9826.00'), 5L)
    }

    void 'employee model is empty for anonymous users'() {
        expect:
        service.employeeModel(null).isEmpty()
    }
}
