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
import io.micronaut.test.annotation.Sql
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import spock.lang.Specification

@Sql('classpath:employees-test-data.sql') // <1>
@MicronautTest(startApplication = false) // <2>
class EmployeeRepositorySpec extends Specification implements TestPropertyProvider { // <3>

    @Inject
    EmployeeRepository repository

    @Inject
    RunAsUser runAsUser // <4>

    @Override
    @NonNull
    Map<String, String> getProperties() {
        Oracle.getProperties()
    }

    void 'findAll returns every employee'() {
        expect:
        runAsUser.sergio { repository.findAll().size() } == 5
    }

    void 'getSalaryAggregates runs as compensation analyst'() {
        expect:
        runAsUser.sergio { repository.salaryAggregates } == new SalaryAggregates(new BigDecimal('6900'), new BigDecimal('13000'), new BigDecimal('49130'), 5L) // <5>
    }
}
