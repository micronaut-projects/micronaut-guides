package example.micronaut.repository

import example.micronaut.domain.Thing
import example.micronaut.repository.ThingRepository
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.util.UUID
import java.util.stream.Collectors

@MicronautTest
class ThingRepositoryTest(private val thingRepository: ThingRepository) {

    @Test
    fun testFindAll() {

        // clear out existing data; safe because each
        // test runs in a transaction that's rolled back
        thingRepository.deleteAll()
        assertEquals(0, thingRepository.count())

        thingRepository.saveAll(listOf(
                Thing("t1"),
                Thing("t2"),
                Thing("t3")))

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
}
