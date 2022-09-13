package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.multipart.MultipartBody
import jakarta.inject.Inject
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.file.Files
import java.nio.file.Path

abstract class AbstractUploadControllerSpec extends Specification {

    @Inject
    @Client('/')
    HttpClient client

    @Unroll
    void "controller path at #controllerPath works"(String controllerPath) {
        given:
        Path path = createTempFile()
        File file = path.toFile()
        MultipartBody requestBody = MultipartBody
                .builder()
                .addPart("fileUpload", file.name, MediaType.TEXT_PLAIN_TYPE, file)
                .build()
        HttpRequest request = HttpRequest
                .POST("${controllerPath}/", requestBody)
                .contentType(MediaType.MULTIPART_FORM_DATA_TYPE)

        when:
        HttpResponse<String> response = client.toBlocking().exchange(request, String)

        then:
        response.status() == HttpStatus.CREATED
        response.body.present
        response.header("ETag")

        where:
        controllerPath << ["/cloud", "/portable"]
    }

    static Path createTempFile() {
        Path path = Files.createTempFile('test-file', '.txt')
        path.toFile().text = "micronaut"
        return path
    }
}
