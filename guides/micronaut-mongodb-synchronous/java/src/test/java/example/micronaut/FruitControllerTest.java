package example.micronaut;

import io.micronaut.context.ApplicationContext;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;

public class FruitControllerTest {

    @Test
    void fruitsEndpointInteractsWithMongo() {
        MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"))
                .withExposedPorts(27017);
        mongoDBContainer.start();

        EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer.class, Collections.singletonMap("mongodb.uri", mongoDBContainer.getReplicaSetUrl()));
        HttpClient httpClient = embeddedServer.getApplicationContext().createBean(HttpClient.class, embeddedServer.getURL());
        BlockingHttpClient client = httpClient.toBlocking();
        List<Fruit> fruits = client.retrieve(HttpRequest.GET("/fruits"), Argument.listOf(Fruit.class));
        assertTrue(fruits.isEmpty());

        HttpResponse<?> response = client.exchange(HttpRequest.POST("/fruits", new Fruit("banana")));
        assertEquals(HttpStatus.CREATED, response.getStatus());
        fruits = client.retrieve(HttpRequest.GET("/fruits"), Argument.listOf(Fruit.class));
        assertFalse(fruits.isEmpty());
        assertEquals("banana", fruits.get(0).getName());
        assertNull(fruits.get(0).getDescription());
        
        response = client.exchange(HttpRequest.POST("/fruits", new Fruit("Apple", "Keeps the doctor away")));
        assertEquals(HttpStatus.CREATED, response.getStatus());
        fruits = client.retrieve(HttpRequest.GET("/fruits"), Argument.listOf(Fruit.class));    
        assertTrue(fruits.stream()
            .filter(f -> f.getDescription() != null && f.getDescription().equals("Keeps the doctor away"))
            .findFirst()
            .isPresent());
        httpClient.close();
        embeddedServer.close();
        mongoDBContainer.close();
    }
}
