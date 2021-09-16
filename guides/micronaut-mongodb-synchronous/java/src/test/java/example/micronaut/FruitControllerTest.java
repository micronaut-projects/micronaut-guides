package example.micronaut;

import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FruitControllerTest implements TestPropertyProvider {
    static MongoDBContainer mongoDBContainer;
    @Inject FruitClient fruitClient;

    @Test
    void fruitsEndpointInteractsWithMongo() {        
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

    @Override
    public Map<String, String> getProperties() {
        mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"))
                                .withExposedPorts(27017);

        mongoDBContainer.start();
        return Collections.singletonMap("mongodb.uri", mongoDBContainer.getReplicaSetUrl());
    }

    @AfterAll
    static void shutdownMongo() {
        if (mongoDBContainer != null) {
            mongoDBContainer.stop();
        }
    }
}
