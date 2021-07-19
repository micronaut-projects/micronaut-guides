package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest  // <1>
public class BasicAuthTest {

    @Inject
    @Client("/")
    HttpClient client; // <2>

    @Test
    void verifyHttpBasicAuthWorks() {
        //when: 'Accessing a secured URL without authenticating'
        Executable e = () -> client.toBlocking().exchange(HttpRequest.GET("/").accept(MediaType.TEXT_PLAIN)); // <3>

        // then: 'returns unauthorized'
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, e); // <4>
        assertEquals(thrown.getStatus(), HttpStatus.UNAUTHORIZED);

        assertTrue(thrown.getResponse().getHeaders().contains("WWW-Authenticate"));
        assertEquals(thrown.getResponse().getHeaders().get("WWW-Authenticate"), "Basic realm=\"Micronaut Guide\"");

        //when: 'A secured URL is accessed with Basic Auth'
        HttpResponse<String> rsp = client.toBlocking().exchange(HttpRequest.GET("/")
                        .accept(MediaType.TEXT_PLAIN)
                        .basicAuth("sherlock", "password"), // <5>
                String.class); // <6>
        //then: 'the endpoint can be accessed'
        assertEquals(rsp.getStatus(),  HttpStatus.OK);
        assertEquals(rsp.getBody().get(),  "sherlock"); // <7>
    }
}
