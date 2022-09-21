package example.micronaut;

import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest // <1>
class X509Test {

    @Inject
    @Client("/") // <2>
    HttpClient httpClient;

    @Test
    void testClientCert() {
        String response = httpClient.toBlocking().retrieve("/"); // <3>
        String expected = "Hello myusername (X.509 cert issued by CN=micronaut.guide.x509)";
        assertEquals(expected, response); // <4>
    }
}
