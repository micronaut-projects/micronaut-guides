package example.micronaut;

import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@MicronautTest
@TestInstance(Lifecycle.PER_CLASS)
public class FruitControllerTest implements TestPropertyProvider {

    @Test
    void fruitsEndpointInteractsWithMongo(FruitClient fruitClient) {
        List<Fruit> fruits = fruitClient.findAll();
        assertTrue(fruits.isEmpty());

        HttpStatus status = fruitClient.save(new Fruit("banana"));

        assertEquals(HttpStatus.CREATED, status);
        fruits = fruitClient.findAll();
        assertFalse(fruits.isEmpty());
        assertEquals("banana", fruits.get(0).getName());
        assertNull(fruits.get(0).getDescription());

        status = fruitClient.save(new Fruit("Apple", "Keeps the doctor away"));
        assertEquals(HttpStatus.CREATED, status);
        fruits = fruitClient.findAll();
        assertTrue(fruits.stream()
                .filter(f -> f.getDescription() != null && f.getDescription().equals("Keeps the doctor away"))
                .findFirst()
                .isPresent());
    }

    @AfterAll
    static void cleanup() {
        MongoDbUtils.closeMongoDb();
    }

    @Override
    public Map<String, String> getProperties() {
        MongoDbUtils.startMongoDb();
        return Collections.singletonMap("mongodb.uri", MongoDbUtils.getMongoDbUri());
    }
}