//tag::imports[]
package example.micronaut;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.type.Argument;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Property(name = "endpoints.refresh.enabled", value = StringUtils.TRUE) // <1>
@Property(name = "endpoints.refresh.sensitive", value = StringUtils.FALSE) // <2>
@MicronautTest // <3>
class RefreshableScopeTest {
    @Inject
    @Client("/")
    HttpClient httpClient; // <4>
    //end::imports[]
    //tag::test[]
    @Test
    void refreshableScopeIsACustomScopeThatAllowsABeansStateToBeRefreshedViaTheRefreshEndpoint() {

        String path = "/refreshable";
        BlockingHttpClient client = httpClient.toBlocking();
        Set<String> responses = new HashSet<>(executeRequest(client, path));
        assertEquals(1, responses.size()); // <5>
        responses.addAll(executeRequest(client, path));
        assertEquals(1, responses.size()); // <6>
        refresh(client); // <7>
        responses.addAll(executeRequest(client, path));
        assertEquals(2, responses.size()); // <8>
    }

    private void refresh(BlockingHttpClient client) {
        client.exchange(HttpRequest.POST("/refresh", 
                        Collections.singletonMap("force", true)));
    }

    private List<String> executeRequest(BlockingHttpClient client, String path) {
        return client.retrieve(HttpRequest.GET(path), 
                               Argument.listOf(String.class));
    }
}
//end::test[]      
