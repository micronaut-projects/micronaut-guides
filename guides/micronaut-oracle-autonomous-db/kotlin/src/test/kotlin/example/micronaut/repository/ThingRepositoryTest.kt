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
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID
import java.util.stream.Collectors

class ThingRepositoryTest {

    private lateinit var applicationContext: ApplicationContext
    private lateinit var thingRepository: ThingRepository

    @BeforeEach
    fun setup() {
        applicationContext = ApplicationContext.run()
        thingRepository = applicationContext.getBean(ThingRepository::class.java)
    }

    @Test
    fun testFindAll() {

        // clear out existing data; safe because each
        // test runs in a transaction that's rolled back
        thingRepository.deleteAll()
        assertEquals(0, thingRepository.count())

        thingRepository.saveAll(listOf(
                Thing("t1"),
                Thing("t2"),
                Thing("t3"))
        )

        val things = thingRepository.findAll()
        assertEquals(3, things.size)
        assertEquals(
                listOf("t1", "t2", "t3"),
                things.stream()
                        .map(Thing::name)
                        .sorted()
                        .collect(Collectors.toList()))
    }

    @Test
    fun testFindByName() {

        val name = UUID.randomUUID().toString()

        var thing = thingRepository.findByName(name).orElse(null)
        assertNull(thing)

        thingRepository.save(Thing(name))
        thing = thingRepository.findByName(name).orElse(null)
        assertNotNull(thing)
        assertEquals(name, thing.name)
    }

    @AfterEach
    fun cleanup() {
        applicationContext.close()
    }
}
