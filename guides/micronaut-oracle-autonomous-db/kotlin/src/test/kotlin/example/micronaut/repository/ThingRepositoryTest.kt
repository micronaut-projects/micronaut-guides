package example.micronaut.repository

import example.micronaut.domain.Thing
import example.micronaut.repository.Oracle.configuration
import io.micronaut.context.ApplicationContext
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIf
import org.testcontainers.DockerClientFactory
import java.util.*
import java.util.stream.Collectors

class ThingRepositoryTest {

    /**
     * WARN  t.gvenzl/oracle-xe:21-slim-faststart - The architecture 'amd64' for image 'gvenzl/oracle-xe:21-slim-faststart'
     * (ID sha256:395e7780aaba5f8c33082bf533a17a4bffdb7bcdd58034702a1634fcbd3d1137) does not match the Docker server architecture 'arm64'.
     * This will cause the container to execute much more slowly due to emulation and may lead to timeout failures.
     */
    fun dockerArchitecture(): Boolean {
        val info = DockerClientFactory.instance().info
        val architecture = info.architecture ?: return true
        return architecture == "x86_64"
    }

    @EnabledIf("dockerArchitecture")
    @Test
    fun testFindAll() {
        val applicationContext = ApplicationContext.run(configuration)
        val thingRepository = applicationContext.getBean(ThingRepository::class.java)

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

        applicationContext.close()
    }

    @EnabledIf("dockerArchitecture")
    @Test
    fun testFindByName() {
        val applicationContext = ApplicationContext.run(configuration)
        val thingRepository = applicationContext.getBean(ThingRepository::class.java)

        val name = UUID.randomUUID().toString()

        var thing = thingRepository.findByName(name).orElse(null)
        assertNull(thing)

        thingRepository.save(Thing(name))
        thing = thingRepository.findByName(name).orElse(null)
        assertNotNull(thing)
        assertEquals(name, thing.name)

        applicationContext.close()
    }
}
