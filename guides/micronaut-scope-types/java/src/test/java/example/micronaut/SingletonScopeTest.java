//tag::imports[]
package example.micronaut;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest // <1>
class SingletonScopeTest {
    @Inject
    @Client("/")
    HttpClient httpClient; // <2>
//end::imports[]
/*
//tag::testheader[]
    @ParameterizedTest
    @ValueSource(strings = {"/singleton"})
//end::testheader[]
*/        
    @ParameterizedTest
    @ValueSource(strings = {"/singleton", "/infrastructuresingleton"})
//tag::test[]    
    void onlyOneInstanceOfTheBeanExistsForSingletonBeans(String path) {
        BlockingHttpClient client = httpClient.toBlocking();
        Set<String> responses = new HashSet<>(executeRequest(client, path));
        assertEquals(1, responses.size()); // <3>
        responses.addAll(executeRequest(client, path));
        assertEquals(1, responses.size()); // <4>
    }

    List<String> executeRequest(BlockingHttpClient client, String path) {
        return client.retrieve(HttpRequest.GET(path), 
          Argument.listOf(String.class));
    }
}
//end::test[]
