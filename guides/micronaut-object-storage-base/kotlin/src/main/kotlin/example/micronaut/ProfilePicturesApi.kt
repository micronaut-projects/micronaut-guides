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

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Status
import io.micronaut.http.multipart.CompletedFileUpload
import io.micronaut.http.server.types.files.StreamedFile
import java.util.Optional

//tag::class[]
interface ProfilePicturesApi {

    @Post(uri = "/{userId}", consumes = [MediaType.MULTIPART_FORM_DATA]) // <1>
    fun upload(fileUpload: CompletedFileUpload, userId: String, request: HttpRequest<*>): HttpResponse<*>

    @Get("/{userId}") // <2>
    fun download(userId: String): Optional<HttpResponse<StreamedFile>>

    @Status(HttpStatus.NO_CONTENT) // <3>
    @Delete("/{userId}") // <4>
    fun delete(userId: String)
}
//end::class[]
