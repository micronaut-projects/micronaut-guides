package example.micronaut

import example.micronaut.domain.Thing
import example.micronaut.repository.ThingRepository
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification

import javax.inject.Inject
import java.util.stream.Collectors

@MicronautTest
class ThingRepositorySpec extends Specification {

    @Inject
    ThingRepository thingRepository

    void 'test findAll'() {

        when:
        // clear out existing data; safe because each
        // test runs in a transaction that's rolled back
        thingRepository.deleteAll()

        then:
        !thingRepository.count()

        when:
        thingRepository.saveAll(Arrays.asList(
                new Thing('t1'),
                new Thing('t2'),
                new Thing('t3')))

        List<Thing> things = thingRepository.findAll()

        then:
        things.size() == 3
        ['t1', 't2', 't3'] == things.stream()
                .map(Thing::getName)
                .sorted()
                .collect(Collectors.toList())
    }

    void 'test findByName'() {
        given:
        String name = UUID.randomUUID()

        when:
        Thing thing = thingRepository.findByName(name).orElse(null)

        then:
        !thing

        when:
        thingRepository.save(new Thing(name))
        thing = thingRepository.findByName(name).orElse(null)

        then:
        thing
        name == thing.name
    }
}
