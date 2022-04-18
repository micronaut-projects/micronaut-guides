package example.micronaut.controllers;

import example.micronaut.TestUtils;
import io.micronaut.context.annotation.Property;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.core.util.StringUtils;
import example.micronaut.entities.Room;
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

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE)
@MicronautTest(transactional = false)
class RoomsControllerTest implements TestUtils {
    @Inject
    @Client("/")
    HttpClient httpClient;

    @Inject
    ResourceLoader resourceLoader;

    @Inject
    RoomRepository roomRepository;

    @Test
    void rootIndexRendersTable() throws IOException {

        BlockingHttpClient client = httpClient.toBlocking();
        HttpResponse<String> response = client.exchange(HttpRequest.GET("/rooms"), String.class);
        assertEquals(HttpStatus.OK, response.status());
        String html = response.body();

        Optional<String> expectedHtmlOptional = getResourceAsString("rooms-index.html");
        assertTrue(expectedHtmlOptional.isPresent());
        String expectedHtml = expectedHtmlOptional.get();
        assertEquals(expectedHtml, html);
    }

    @Test
    void roomsCreatesRendersForm() throws IOException {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = HttpRequest.GET(UriBuilder.of("/rooms").path("create").build());
        HttpResponse<String> response = client.exchange(request, String.class);
        assertEquals(HttpStatus.OK, response.status());
        String html = response.body();

        Optional<String> expectedHtmlOptional = getResourceAsString("rooms-create.html");
        assertTrue(expectedHtmlOptional.isPresent());
        String expectedHtml = expectedHtmlOptional.get();
        assertEquals(expectedHtml, html);
    }

    @Test
    void roomsEditRendersForm() throws IOException {
        Room roomA = roomRepository.save("Room A");
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = HttpRequest.GET(UriBuilder.of("/rooms").path("" + roomA.getId()).path("edit").build());
        HttpResponse<String> response = client.exchange(request, String.class);
        assertEquals(HttpStatus.OK, response.status());
        String html = response.body();


        Optional<String> expectedHtmlOptional = getResourceAsString("rooms-edit.html");
        assertTrue(expectedHtmlOptional.isPresent());
        String expectedHtml = expectedHtmlOptional.get();
        expectedHtml = expectedHtml.replaceAll("ROOM_ID", "" + roomA.getId());
        expectedHtml = expectedHtml.replaceAll("ROOM_NAME", roomA.getName());
        assertEquals(expectedHtml, html);
        roomRepository.delete(roomA);
    }

    @Test
    void roomDelete() {
        Room roomA = roomRepository.save("Room A");
        long oldCount = roomRepository.count();
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = HttpRequest.POST(UriBuilder.of("/rooms").path("" + roomA.getId()).path("delete").build(), Collections.emptyMap())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        HttpResponse<?> response = client.exchange(request);
        assertEquals(HttpStatus.SEE_OTHER, response.status());
        String location = response.getHeaders().get(HttpHeaders.LOCATION);
        assertNotNull(location);
        assertEquals("/rooms", location);
        assertEquals(oldCount - 1, roomRepository.count());
    }

    @Test
    void roomUpdate() {
        Room roomA = roomRepository.save("Room A");
        BlockingHttpClient client = httpClient.toBlocking();
        String name = "Room B";
        Map<String, Object> body = new HashMap<>();
        body.put("id", roomA.getId());
        body.put("name", name);
        HttpRequest<?> request = HttpRequest.POST(UriBuilder.of("/rooms").path("update").build(), body)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        HttpResponse<?> response = client.exchange(request);
        assertEquals(HttpStatus.SEE_OTHER, response.status());
        String location = response.getHeaders().get(HttpHeaders.LOCATION);
        assertNotNull(location);
        assertTrue(location.startsWith("/rooms/"));
        assertEquals(name, roomRepository.findById(roomA.getId()).get().getName());
        roomRepository.deleteById(roomA.getId());
    }

    @Test
    void roomSave() {
        String name = "Room A";
        long oldCount = roomRepository.count();
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = HttpRequest.POST("/rooms", Collections.singletonMap("name", name))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        HttpResponse<?> response = client.exchange(request);
        assertEquals(HttpStatus.SEE_OTHER, response.status());
        String location = response.getHeaders().get(HttpHeaders.LOCATION);
        assertNotNull(location);
        assertTrue(location.startsWith("/rooms/"));
        assertEquals(oldCount + 1, roomRepository.count());
        roomRepository.deleteAll();
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return this.resourceLoader;
    }
}