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

import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification
import io.micronaut.function.aws.MicronautRequestHandler
import io.micronaut.serde.annotation.Serdeable
import jakarta.inject.Inject
import org.slf4j.LoggerFactory
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.util.Locale

@Serdeable
class FunctionRequestHandler : MicronautRequestHandler<S3EventNotification, Void?>() { // <1>

    @Inject
    lateinit var s3Client: S3Client // <2>

    @Inject
    lateinit var thumbnailGenerator: ThumbnailGenerator // <3>

    override fun execute(input: S3EventNotification): Void? {
        for (record in input.records) {
            LOG.info("event name: {}", record.eventName)
            if (record.eventName.contains(OBJECT_CREATED)) { // <4>
                val s3Entity = record.s3
                val bucket = s3Entity.bucket.name
                val key = s3Entity.`object`.key
                val index = key.lastIndexOf(DOT)
                if (index != -1) {
                    val format = key.substring(index + 1).lowercase(Locale.ENGLISH)
                    if (format == PNG || format == JPG) {
                        thumbnailGenerator.thumbnail(
                            s3Client.getObject(
                                GetObjectRequest.builder()
                                    .bucket(bucket)
                                    .key(key)
                                    .build()
                            ),
                            format
                        )?.let { bytes ->
                            s3Client.putObject(
                                PutObjectRequest.builder()
                                    .key(thumbnailKey(key))
                                    .bucket(bucket)
                                    .build(),
                                RequestBody.fromBytes(bytes)
                            )
                        }
                    }
                }
            }
        }
        return null
    }

    private fun thumbnailKey(key: String): String {
        val index = key.lastIndexOf(SLASH)
        val fileName = if (index != -1) key.substring(index + SLASH.length) else key
        return "$THUMBNAILS$SLASH$fileName"
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(FunctionRequestHandler::class.java)
        private const val SLASH = "/"
        private const val THUMBNAILS = "thumbnails"
        const val OBJECT_CREATED = "ObjectCreated"
        private const val JPG = "jpg"
        private const val PNG = "png"
        private const val DOT = '.'
    }
}
