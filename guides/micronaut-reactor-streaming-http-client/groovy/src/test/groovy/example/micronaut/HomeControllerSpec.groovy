/*
 * Copyright 2017-2026 original authors
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

import io.micronaut.core.io.ResourceLoader
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest
import java.util.HexFormat

@MicronautTest // <1>
class HomeControllerSpec extends Specification {

    private final HexFormat hexFormat = HexFormat.of()

    @Inject
    @Client("/") // <2>
    HttpClient httpClient

    @Inject
    ResourceLoader resourceLoader

    void "download file"() {
        when:
        Optional<URL> resource = resourceLoader.getResource('micronaut5K.png')

        then:
        resource.present

        when:
        byte[] expectedByteArray = Files.readAllBytes(Path.of(resource.get().toURI()))
        MessageDigest digest = MessageDigest.getInstance('SHA-256')
        String expected = hexFormat.formatHex(digest.digest(expectedByteArray))

        HttpResponse<byte[]> resp = httpClient.toBlocking()
                .exchange(HttpRequest.GET('/'), byte[])
        byte[] responseBytes = resp.body()

        String response = hexFormat.formatHex(digest.digest(responseBytes))

        then:
        expected == response
    }
}
