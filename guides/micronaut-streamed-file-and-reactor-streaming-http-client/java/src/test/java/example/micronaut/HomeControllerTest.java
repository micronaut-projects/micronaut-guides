package example.micronaut;

import io.micronaut.core.io.ResourceLoader;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.condition.DisabledInNativeImage;

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
        String expected = bytesToHex(expectedEncodedHash);

        BlockingHttpClient client = httpClient.toBlocking();
        HttpResponse<byte[]> resp = assertDoesNotThrow(() -> client.exchange(HttpRequest.GET("/"), byte[].class));
        byte[] responseBytes = resp.body();

        byte[] responseEncodedHash = digest.digest(responseBytes);
        String response = bytesToHex(responseEncodedHash);
        assertEquals(expected, response);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}