/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

import java.nio.file.Files
import java.nio.file.Path

abstract class AbstractProfilePicturesControllerSpec extends Specification {

    @Inject
    @Client('/')
    HttpClient client

    void "it works"() {
        given:
        Path path = createTempFile()
        File file = path.toFile()
        MultipartBody requestBody = MultipartBody
                .builder()
                .addPart("fileUpload", file.name, MediaType.TEXT_PLAIN_TYPE, file)
                .build()

        String uri = "${ProfilePicturesController.PREFIX}/alvaro"
        HttpRequest request = HttpRequest
                .POST(uri, requestBody)
                .contentType(MediaType.MULTIPART_FORM_DATA_TYPE)

        when:
        HttpResponse<String> response = client.toBlocking().exchange(request, String)

        then:
        response.status() == HttpStatus.CREATED
        response.header("location")
        response.header("ETag")
        assertThatFileIsStored("alvaro.jpg", "micronaut")

        when:
        File download = client.toBlocking().retrieve(uri, File.class)

        then:
        file.text == "micronaut"

        when:
        response = client.toBlocking().exchange(HttpRequest.DELETE(uri))

        then:
        response
    }

    static Path createTempFile() {
        Path path = Files.createTempFile('test-file', '.txt')
        path.toFile().text = "micronaut"
        return path
    }

    abstract boolean assertThatFileIsStored(String key, String text)
}
