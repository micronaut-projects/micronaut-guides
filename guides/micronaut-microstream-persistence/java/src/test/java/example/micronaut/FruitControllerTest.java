package example.micronaut;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FruitControllerTest extends BaseMicroStreamTest {

    @Test
    void emptyDatabaseContainsNoFruit() {
        assertEquals(0, StreamSupport.stream(fruitClient.list().spliterator(), false).count());
    }

    @Test
    void testInteractionWithTheController() {
        HttpResponse<Fruit> response = fruitClient.create(new FruitCommand("banana", null));
        assertEquals(HttpStatus.CREATED, response.getStatus());
        Fruit banana = response.getBody().get();

        Iterable<Fruit> fruits = fruitClient.list();

        List<Fruit> fruitList = StreamSupport.stream(fruits.spliterator(), false).collect(Collectors.toList());
        assertEquals(1, fruitList.size());
        assertEquals(banana.getName(), fruitList.get(0).getName());
        assertNull(fruitList.get(0).getDescription());

        response = fruitClient.create(new FruitCommand("apple", "Keeps the doctor away"));
        assertEquals(HttpStatus.CREATED, response.getStatus());

        fruits = fruitClient.list();
        assertTrue(StreamSupport.stream(fruits.spliterator(), false)
                .anyMatch(f -> "Keeps the doctor away".equals(f.getDescription())));

        fruitClient.update(new FruitCommand("banana", "Yellow and curved"));

        fruits = fruitClient.list();

        assertEquals(
                Stream.of("Keeps the doctor away", "Yellow and curved").collect(Collectors.toSet()),
                StreamSupport.stream(fruits.spliterator(), false)
                        .map(Fruit::getDescription)
                        .collect(Collectors.toSet())
        );
    }
}
