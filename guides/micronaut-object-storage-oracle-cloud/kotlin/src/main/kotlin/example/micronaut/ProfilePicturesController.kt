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

import com.oracle.bmc.objectstorage.responses.GetObjectResponse
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.multipart.CompletedFileUpload
import io.micronaut.http.server.types.files.StreamedFile
import io.micronaut.http.server.util.HttpHostResolver
import io.micronaut.http.uri.UriBuilder
import io.micronaut.objectstorage.oraclecloud.OracleCloudStorageEntry
import io.micronaut.objectstorage.oraclecloud.OracleCloudStorageOperations
import io.micronaut.objectstorage.request.UploadRequest
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import java.net.URI
import java.util.Optional

//tag::begin-class[]
@Controller(ProfilePicturesController.PREFIX) // <1>
@ExecuteOn(TaskExecutors.BLOCKING) // <2>
class ProfilePicturesController(
    private val objectStorage: OracleCloudStorageOperations, // <3>
    private val httpHostResolver: HttpHostResolver // <4>
) : ProfilePicturesApi {

    companion object {
        const val PREFIX = "/pictures"
    }
//end::begin-class[]

    //tag::upload[]
    override fun upload(fileUpload: CompletedFileUpload, userId: String, request: HttpRequest<*>): HttpResponse<*> {
        val key = buildKey(userId) // <1>
        val objectStorageUpload = UploadRequest.fromCompletedFileUpload(fileUpload, key) // <2>
        val response = objectStorage.upload(objectStorageUpload) // <3>

        return HttpResponse
            .created<Any>(location(request, userId)) // <4>
            .header(HttpHeaders.ETAG, response.getETag()) // <5>
    }

    private fun buildKey(userId: String): String = "$userId.jpg"

    private fun location(request: HttpRequest<*>, userId: String): URI =
        UriBuilder.of(httpHostResolver.resolve(request))
            .path(PREFIX)
            .path(userId)
            .build()
    //end::upload[]

    //tag::download[]
    override fun download(userId: String): Optional<HttpResponse<StreamedFile>> {
        val key = buildKey(userId)
        return objectStorage.retrieve(key) // <1>
            .map { entry -> buildStreamedFile(entry) } // <2>
    }

    private fun buildStreamedFile(entry: OracleCloudStorageEntry): HttpResponse<StreamedFile> {
        val nativeEntry: GetObjectResponse = entry.nativeEntry
        val mediaType = MediaType.of(nativeEntry.contentType)
        val file = StreamedFile(entry.inputStream, mediaType).attach(entry.key)
        val httpResponse: MutableHttpResponse<Any> = HttpResponse.ok<Any>()
            .header(HttpHeaders.ETAG, nativeEntry.eTag) // <3>
        file.process(httpResponse)
        return httpResponse.body(file)
    }
    //end::download[]

    //tag::delete[]
    override fun delete(userId: String) {
        val key = buildKey(userId)
        objectStorage.delete(key)
    }
    //end::delete[]

//tag::end-class[]
}
//end::end-class[]
