package example.micronaut;

import io.micronaut.context.ApplicationContext;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;

public class FruitControllerTest {

    @Test
    void fruitsEndpointInteractsWithMongo() {        
        EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer.class, 
            Collections.singletonMap("mongodb.uri", MongoDbUtils.getMongoDbUri()));
        FruitClient fruitClient = embeddedServer.getApplicationContext()
            .getBean(FruitClient.class);

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
        embeddedServer.close();
        MongoDbUtils.closeMongoDb();
    }
}
