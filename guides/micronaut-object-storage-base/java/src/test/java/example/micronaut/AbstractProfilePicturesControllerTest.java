package example.micronaut;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.multipart.MultipartBody;
import io.micronaut.http.uri.UriBuilder;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

abstract class AbstractProfilePicturesControllerTest {

    @Inject
    @Client("/")
    HttpClient httpClient;

    @Test
    void itWorks() throws IOException {
        //given:
        BlockingHttpClient client = httpClient.toBlocking();
        Path path = createTempFile();
        File file = path.toFile();
        MultipartBody requestBody = MultipartBody
                .builder()
                .addPart("fileUpload", file.getName(), MediaType.TEXT_PLAIN_TYPE, file)
                .build();
        String uri = UriBuilder.of(ProfilePicturesController.PREFIX).path("alvaro").build().toString();
        HttpRequest request = HttpRequest
                .POST(uri, requestBody)
                .contentType(MediaType.MULTIPART_FORM_DATA_TYPE);

        //when:
        HttpResponse<String> response = client.exchange(request, String.class);

        //then:
        assertEquals(response.status(), HttpStatus.CREATED);
        assertNotNull(response.header(HttpHeaders.ETAG));
        assertThatFileIsStored("alvaro.jpg", "micronaut");

        //when:
        String location = response.header(HttpHeaders.LOCATION);

        //then:
        assertNotNull(location);

        //when:
        byte[] download = client.retrieve(location, byte[].class);

        //then:
        assertEquals(new String(download), "micronaut");

        //when:
        response = client.exchange(HttpRequest.DELETE(location));

        //then:
        assertEquals(HttpStatus.NO_CONTENT, response.status());
    }

    static Path createTempFile() throws IOException {
        Path path = Files.createTempFile("test-file", ".txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()));
        writer.write("micronaut");
        writer.close();
        return path;
    }

    protected String textFromFile(InputStream inputStream) throws IOException {
        return textFromFile(new InputStreamReader(inputStream));
    }

    protected String textFromFile(Reader reader) throws IOException {
        BufferedReader br = new BufferedReader(reader);
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }

    protected String textFromFile(File f) throws IOException {
        return textFromFile(new BufferedReader(new FileReader(f)));
    }

    protected abstract boolean assertThatFileIsStored(String key, String text) throws IOException;
}
