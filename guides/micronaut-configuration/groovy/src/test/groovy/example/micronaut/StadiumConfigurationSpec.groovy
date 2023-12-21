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
package example.micronaut

import example.micronaut.StadiumConfiguration
import io.micronaut.context.ApplicationContext
import io.micronaut.inject.qualifiers.Qualifiers
import spock.lang.Specification

class StadiumConfigurationSpec extends Specification {

    void "test stadium configuration"() {
        given:
        ApplicationContext ctx = ApplicationContext.run(ApplicationContext, [
                "stadium.fenway.city": 'Boston', // <1>
                "stadium.fenway.size": 60000,
                "stadium.wrigley.city": 'Chicago',
                "stadium.wrigley.size": 45000
        ])

        when:
        // <2>
        StadiumConfiguration fenwayConfiguration = ctx.getBean(StadiumConfiguration, Qualifiers.byName("fenway"))
        StadiumConfiguration wrigleyConfiguration = ctx.getBean(StadiumConfiguration, Qualifiers.byName("wrigley"))

        then:
        fenwayConfiguration.name == "fenway"
        fenwayConfiguration.size == 60000
        wrigleyConfiguration.name == "wrigley"
        wrigleyConfiguration.size == 45000

        cleanup:
        ctx.close()
    }
}
