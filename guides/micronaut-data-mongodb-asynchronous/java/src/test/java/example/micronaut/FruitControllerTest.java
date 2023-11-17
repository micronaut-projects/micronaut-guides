package example.micronaut;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(transactional = false) // <1>
class FruitControllerTest {

    @Inject
    FruitClient fruitClient;

    @Inject
    FruitRepository fruitRepository;

    @AfterEach // <2>
    void cleanup() {
        Flux.from(fruitRepository.deleteAll()).blockFirst();
    }

    @Test
    void emptyDatabaseContainsNoFruit() {
        assertEquals(0, StreamSupport.stream(fruitClient.list().spliterator(), false).count());
    }

    @Test
    void testInteractionWithTheController() {
        HttpResponse<Fruit> response = fruitClient.save(new Fruit("banana", null));
        assertEquals(HttpStatus.CREATED, response.getStatus());
        Fruit banana = response.getBody().get();

        Iterable<Fruit> fruits = fruitClient.list();
        List<Fruit> fruitList = StreamSupport.stream(fruits.spliterator(), false).toList();
        assertEquals(1, fruitList.size());
        assertEquals(banana.getName(), fruitList.get(0).getName());
        assertNull(fruitList.get(0).getDescription());

        response = fruitClient.save(new Fruit("apple", "Keeps the doctor away"));
        assertEquals(HttpStatus.CREATED, response.getStatus());

        fruits = fruitClient.list();
        assertTrue(StreamSupport.stream(fruits.spliterator(), false)
                .anyMatch(f -> "Keeps the doctor away".equals(f.getDescription())));

        banana.setDescription("Yellow and curved");
        fruitClient.update(banana);

        fruits = fruitClient.list();

        assertEquals(
                Stream.of("Keeps the doctor away", "Yellow and curved").collect(Collectors.toSet()),
                StreamSupport.stream(fruits.spliterator(), false)
                        .map(Fruit::getDescription)
                        .collect(Collectors.toSet())
        );
    }

    @Test
    void testSearchWorksAsExpected() {
        fruitClient.save(new Fruit("apple", "Keeps the doctor away"));
        fruitClient.save(new Fruit("pineapple", "Delicious"));
        fruitClient.save(new Fruit("lemon", "Lemonentary my dear Dr Watson"));

        Iterable<Fruit> fruit = fruitClient.query(Arrays.asList("apple", "pineapple"));

        assertTrue(StreamSupport.stream(fruit.spliterator(), false)
                .allMatch(f -> f.getName().equals("apple") || f.getName().equals("pineapple")));
    }
}
