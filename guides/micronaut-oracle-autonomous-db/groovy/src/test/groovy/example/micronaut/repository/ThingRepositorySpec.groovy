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
package example.micronaut.repository

import example.micronaut.domain.Thing
import io.micronaut.context.ApplicationContext
import spock.lang.Specification

class ThingRepositorySpec extends Specification {

    void testFindAll() {
        given:
        ApplicationContext applicationContext = ApplicationContext.run()
        ThingRepository thingRepository = applicationContext.getBean(ThingRepository.class)

        // clear out existing data; safe because each
        // test runs in a transaction that's rolled back
        when:
        thingRepository.deleteAll()

        then:
        0 == thingRepository.count()

        when:
        thingRepository.saveAll(Arrays.asList(
                new Thing("t1"),
                new Thing("t2"),
                new Thing("t3")))
        List<Thing> things = thingRepository.findAll()

        then:
        3 == things.size()
        Arrays.asList("t1", "t2", "t3") ==
                things.stream()
                        .map(Thing::getName)
                        .sorted()
                        .toList()
        cleanup:
        applicationContext.close()
    }

    void testFindByName() {
        given:
        ApplicationContext applicationContext = ApplicationContext.run()
        ThingRepository thingRepository = applicationContext.getBean(ThingRepository.class)
        String name = UUID.randomUUID().toString()
        when:
        Thing thing = thingRepository.findByName(name).orElse(null);
        then:
        !thing

        when:
        thingRepository.save(new Thing(name));
        thing = thingRepository.findByName(name).orElse(null);

        then:
        thing
        name == thing.name

        cleanup:
        applicationContext.close()
    }
}
