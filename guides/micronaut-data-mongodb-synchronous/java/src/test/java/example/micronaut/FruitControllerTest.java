package example.micronaut;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FruitControllerTest extends BaseMongoDataTest {

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

        List<Fruit> fruitList = StreamSupport.stream(fruits.spliterator(), false).collect(Collectors.toList());
        assertEquals(1, fruitList.size());
        assertEquals(banana.getName(), fruitList.get(0).getName());
        assertNull(fruitList.get(0).getDescription());

        response = fruitClient.save(new Fruit("apple", "Keeps the doctor away"));
        assertEquals(HttpStatus.CREATED, response.getStatus());

        fruits = fruitClient.list();
        assertTrue(StreamSupport.stream(fruits.spliterator(), false).anyMatch(f -> "Keeps the doctor away".equals(f.getDescription())));

        banana.setDescription("Yellow and curved");
        fruitClient.update(banana);

        fruits = fruitClient.list();

        assertEquals(Set.of("Keeps the doctor away", "Yellow and curved"), StreamSupport.stream(fruits.spliterator(), false).map(Fruit::getDescription).collect(Collectors.toSet()));
    }

    @Test
    void testSearchWorksAsExpected() {
        fruitClient.save(new Fruit("apple", "Keeps the doctor away"));
        fruitClient.save(new Fruit("pineapple", "Delicious"));
        fruitClient.save(new Fruit("lemon", "Lemonentary my dear Dr Watson"));

        Iterable<Fruit> fruit = fruitClient.query(List.of("apple", "pineapple"));

        assertTrue(StreamSupport.stream(fruit.spliterator(), false).allMatch(f -> f.getName().equals("apple") || f.getName().equals("pineapple")));
    }
}
