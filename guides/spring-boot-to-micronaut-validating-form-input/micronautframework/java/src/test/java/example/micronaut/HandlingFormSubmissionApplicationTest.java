package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class HandlingFormSubmissionApplicationTest {

    @Test
    void resultsRendersAnHtmlPage(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        String html = client.retrieve("/results");
        assertTrue(html.contains("Congratulations! You are old enough to sign up for this site."));
    }

    @Test
    void rootRendersAnHtmlPageWithAForm(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        String html = client.retrieve("/");
        assertTrue(html.contains("Name:"));
        assertTrue(html.contains("name=\"name\" value=\"\""));
        assertTrue(html.contains("name=\"age\" value=\"\""));

    }

    @Test
    void formWithErrors(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = HttpRequest.POST("/", Map.of("name", "N", "age", "15"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        Executable e = () -> client.exchange(request);
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, e);
        Optional<String> htmlOptional = thrown.getResponse().getBody(String.class);
        assertTrue(htmlOptional.isPresent());
        String html = htmlOptional.get();
        assertNotNull(html);
        assertTrue(html.contains("name=\"name\" value=\"N\""));
        assertTrue(html.contains("name=\"age\" value=\"15\""));
        assertTrue(html.contains("size must be between 2 and 30"));
        assertTrue(html.contains("must be greater than or equal to 18"));
    }

    @Test
    void formWithoutErrors(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = HttpRequest.POST("/", Map.of("name", "Sergio", "age", "25"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        String html = client.retrieve(request);
        assertTrue(html.contains("Congratulations! You are old enough to sign up for this site."));
    }
}