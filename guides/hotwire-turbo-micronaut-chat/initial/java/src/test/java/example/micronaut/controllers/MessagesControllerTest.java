package example.micronaut.controllers;

import example.micronaut.TestUtils;
import io.micronaut.context.annotation.Property;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.core.util.StringUtils;
import example.micronaut.entities.Room;
import example.micronaut.repositories.MessageRepository;
import example.micronaut.repositories.RoomRepository;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE)
@MicronautTest(transactional = false)
public class MessagesControllerTest implements TestUtils {
    @Inject
    @Client("/")
    HttpClient httpClient;

    @Inject
    RoomRepository roomRepository;

    @Inject
    MessageRepository messageRepository;

    @Inject
    ResourceLoader resourceLoader;

    @Override
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    @Test
    void saveAMessage() {
        BlockingHttpClient client = httpClient.toBlocking();
        Room roomA = roomRepository.save("Room A");

        URI uri = UriBuilder.of("/rooms").path("" + roomA.getId()).path("messages").build();
        HttpRequest<?> request = HttpRequest.POST(uri, Collections.singletonMap("content", "Hola"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpResponse<?> response = client.exchange(request);
        assertEquals(HttpStatus.SEE_OTHER, response.status());
        String location = response.getHeaders().get(HttpHeaders.LOCATION);
        assertNotNull(location);
        assertTrue(location.startsWith("/rooms/"));

        messageRepository.deleteAll();
        roomRepository.deleteById(roomA.getId());
    }


    @Test
    void createAMessage() {
        BlockingHttpClient client = httpClient.toBlocking();
        Room roomA = roomRepository.save("Room A");

        URI uri = UriBuilder.of("/rooms")
                .path("" + roomA.getId())
                .path("messages")
                .path("create")
                .build();
        HttpRequest<?> request = HttpRequest.GET(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpResponse<String> response = client.exchange(request, String.class);
        assertEquals(HttpStatus.OK, response.status());
        Optional<String> expectedHtmlOptional = getResourceAsString("messages-create.html");
        assertTrue(expectedHtmlOptional.isPresent());
        String expectedHtml = expectedHtmlOptional.get();
        expectedHtml = expectedHtml.replaceAll("ROOM_ID", "" + roomA.getId());
        String html = response.body();
        assertEquals(expectedHtml, html);

        roomRepository.deleteById(roomA.getId());
    }
}
