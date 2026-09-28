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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest // <1>
class PopulationTest {

    @Autowired // <2>
    DefaultListableBeanFactory beanContext;

    @Autowired // <3>
    Population population;

    @Test
    void canUsePreDestroyHooks() {
        Person john = beanContext.getBean(Person.class);
        Person aegon = beanContext.getBean(Person.class);
        beanContext.destroyBean(aegon);
        beanContext.destroyBean(john);
        assertEquals(2, population.deaths());
    }

}