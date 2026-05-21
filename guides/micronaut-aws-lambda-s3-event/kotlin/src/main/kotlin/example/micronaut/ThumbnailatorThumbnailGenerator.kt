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

import jakarta.inject.Singleton
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import net.coobird.thumbnailator.Thumbnails
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

@Singleton // <1>
open class ThumbnailatorThumbnailGenerator(
    private val thumbnailConfiguration: ThumbnailConfiguration // <2>
) : ThumbnailGenerator {

    override fun thumbnail(
        inputStream: InputStream,
        @NotBlank @Pattern(regexp = "jpg|png") format: String
    ): ByteArray? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        return try {
            Thumbnails.of(inputStream)
                .size(thumbnailConfiguration.width, thumbnailConfiguration.height)
                .outputFormat(format)
                .toOutputStream(byteArrayOutputStream)
            byteArrayOutputStream.toByteArray()
        } catch (e: IOException) {
            LOG.warn("IOException thrown while generating the thumbnail")
            null
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(ThumbnailatorThumbnailGenerator::class.java)
    }
}
