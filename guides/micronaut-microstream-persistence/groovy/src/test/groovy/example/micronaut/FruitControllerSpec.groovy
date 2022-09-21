package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.runtime.server.EmbeddedServer

import java.util.stream.Collectors
import java.util.stream.Stream
import java.util.stream.StreamSupport

class FruitControllerSpec extends BaseSpec {

    void "test interaction with the Controller"() {
        given:
        FruitCommand apple = new FruitCommand("apple", "Keeps the doctor away")
        String bananaName = "banana"
        String bananaDescription = "Yellow and curved"
        Map<String, Object> properties = new HashMap<>(getProperties())
        EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer, properties) // <1>
        FruitClient fruitClient = embeddedServer.getApplicationContext().getBean(FruitClient)

        when:
        HttpResponse<Fruit> response = fruitClient.create(new FruitCommand(bananaName))
        then:
        HttpStatus.CREATED == response.status
        response.body.isPresent()

        when:
        Fruit banana = response.body.get()
        List<Fruit> fruitList = fruitsList(fruitClient)

        then:
        1 == fruitList.size()
        banana.name == fruitList.get(0).name
        !fruitList.get(0).description

        when:
        Optional<Fruit> bananaOptional = fruitClient.update(apple)

        then:
        !bananaOptional.isPresent()

        when:
        response = fruitClient.create(apple)

        then:
        HttpStatus.CREATED == response.status
        fruitsStream(fruitClient)
                    .anyMatch(f -> "Keeps the doctor away" == f.description)

        when:
        bananaOptional = fruitClient.update(new FruitCommand(bananaName, bananaDescription))
        then:
        bananaOptional.isPresent()
        Stream.of("Keeps the doctor away", "Yellow and curved").collect(Collectors.toSet()) ==
                fruitsStream(fruitClient)
                        .map(Fruit::getDescription)
                        .collect(Collectors.toSet())
        when:
        embeddedServer.close()
        embeddedServer = ApplicationContext.run(EmbeddedServer, properties) // <1>
        fruitClient = embeddedServer.getApplicationContext().getBean(FruitClient)

        then:
        2 == numberOfFruits(fruitClient)
        when:
        fruitClient.delete(apple)
        fruitClient.delete(new FruitCommand(bananaName, bananaDescription))
        embeddedServer.close()
        embeddedServer = ApplicationContext.run(EmbeddedServer, properties) // <1>
        fruitClient = embeddedServer.getApplicationContext().getBean(FruitClient)

        then:
        0 == numberOfFruits(fruitClient)

        cleanup:
        embeddedServer.close()
    }

    private int numberOfFruits(FruitClient fruitClient) {
        return fruitsList(fruitClient).size()
    }

    private List<Fruit> fruitsList(FruitClient fruitClient) {
        return fruitsStream(fruitClient)
                .collect(Collectors.toList())
    }

    private Stream<Fruit> fruitsStream(FruitClient fruitClient) {
        Iterable<Fruit> fruits = fruitClient.list()
        return StreamSupport.stream(fruits.spliterator(), false)
    }
}
