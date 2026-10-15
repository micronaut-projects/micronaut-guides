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

import io.micronaut.core.io.IOUtils
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.multipart.MultipartBody
import io.micronaut.http.uri.UriBuilder
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Path

abstract class AbstractProfilePicturesControllerTest {

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Test
    @Throws(IOException::class)
    fun itWorks() {
        //given:
        val client = httpClient.toBlocking()
        val path = createTempFile()
        val file = path.toFile()
        val requestBody = MultipartBody
            .builder()
            .addPart("fileUpload", file.name, MediaType.TEXT_PLAIN_TYPE, file)
            .build()
        val uri = UriBuilder.of(ProfilePicturesController.PREFIX).path("alvaro").build().toString()
        val request: HttpRequest<*> = HttpRequest
            .POST(uri, requestBody)
            .contentType(MediaType.MULTIPART_FORM_DATA_TYPE)

        //when:
        var response: HttpResponse<String> = client.exchange(request, String::class.java)

        //then:
        assertEquals(response.status(), HttpStatus.CREATED)
        assertNotNull(response.header(HttpHeaders.ETAG))
        assertThatFileIsStored("alvaro.jpg", "micronaut")

        //when:
        val location = response.header(HttpHeaders.LOCATION) ?: error("Missing Location header")

        //when:
        val download = client.retrieve(location, ByteArray::class.java)

        //then:
        assertEquals("micronaut", String(download))

        //when:
        response = client.exchange(HttpRequest.DELETE<Any>(location))

        //then:
        assertEquals(HttpStatus.NO_CONTENT, response.status())
    }

    protected fun textFromFile(inputStream: InputStream): String =
        IOUtils.readText(BufferedReader(InputStreamReader(inputStream)))

    @Throws(IOException::class)
    protected abstract fun assertThatFileIsStored(key: String, expected: String)

    companion object {
        @JvmStatic
        @Throws(IOException::class)
        fun createTempFile(): Path {
            val path = Files.createTempFile("test-file", ".txt")
            BufferedWriter(FileWriter(path.toFile())).use { writer ->
                writer.write("micronaut")
            }
            return path
        }
    }
}
