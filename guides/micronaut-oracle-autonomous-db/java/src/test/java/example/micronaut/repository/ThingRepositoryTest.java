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
package example.micronaut.repository;

import example.micronaut.domain.Thing;
import io.micronaut.context.ApplicationContext;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ThingRepositoryTest {

    @Test
    void testFindAll() {
        ApplicationContext applicationContext = ApplicationContext.run();
        ThingRepository thingRepository = applicationContext.getBean(ThingRepository.class);

        // clear out existing data; safe because each
        // test runs in a transaction that's rolled back
        thingRepository.deleteAll();
        assertEquals(0, thingRepository.count());

        thingRepository.saveAll(Arrays.asList(
                new Thing("t1"),
                new Thing("t2"),
                new Thing("t3")));

        List<Thing> things = thingRepository.findAll();
        assertEquals(3, things.size());
        assertEquals(
                Arrays.asList("t1", "t2", "t3"),
                things.stream()
                        .map(Thing::getName)
                        .sorted()
                        .collect(Collectors.toList()));
        applicationContext.close();
    }

    @Test
    void testFindByName() {
        ApplicationContext applicationContext = ApplicationContext.run();
        ThingRepository thingRepository = applicationContext.getBean(ThingRepository.class);

        String name = UUID.randomUUID().toString();

        Thing thing = thingRepository.findByName(name).orElse(null);
        assertNull(thing);

        thingRepository.save(new Thing(name));
        thing = thingRepository.findByName(name).orElse(null);
        assertNotNull(thing);
        assertEquals(name, thing.getName());
        applicationContext.close();
    }
}
