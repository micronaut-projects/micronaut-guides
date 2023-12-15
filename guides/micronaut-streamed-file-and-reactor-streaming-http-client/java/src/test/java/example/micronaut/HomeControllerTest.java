package example.micronaut;

import io.micronaut.core.io.ResourceLoader;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest // <1>
class HomeControllerTest {

    @DisabledInNativeImage
    @Test
    void downloadFile(@Client("/") HttpClient httpClient, // <2>
                      ResourceLoader resourceLoader) // <3>
            throws URISyntaxException, IOException, NoSuchAlgorithmException {
        Optional<URL> resource = resourceLoader.getResource("micronaut5K.png");
        assertTrue(resource.isPresent());
        byte[] expectedByteArray = Files.readAllBytes(Path.of(resource.get().toURI()));

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] expectedEncodedHash = digest.digest(expectedByteArray);
        String expected = HexFormat.of().formatHex(expectedEncodedHash);

        BlockingHttpClient client = httpClient.toBlocking();
        HttpResponse<byte[]> resp = assertDoesNotThrow(() -> client.exchange(HttpRequest.GET("/"), byte[].class));
        byte[] responseBytes = resp.body();

        byte[] responseEncodedHash = digest.digest(responseBytes);
        String response = HexFormat.of().formatHex(responseEncodedHash);
        assertEquals(expected, response);
    }
}