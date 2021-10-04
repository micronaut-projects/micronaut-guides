package example.micronaut;

import example.micronaut.domain.Thing;
import example.micronaut.repository.ThingRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@MicronautTest
class ThingRepositoryTest {

    @Inject
    ThingRepository thingRepository;

    @Test
    void testFindAll() {

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
    }

    @Test
    void testFindByName() {
        String name = UUID.randomUUID().toString();

        Thing thing = thingRepository.findByName(name).orElse(null);
        assertNull(thing);

        thingRepository.save(new Thing(name));
        thing = thingRepository.findByName(name).orElse(null);
        assertNotNull(thing);
        assertEquals(name, thing.getName());
    }
}
