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

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.testcontainers.junit.jupiter.Testcontainers
import software.amazon.awssdk.core.ResponseInputStream
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.CreateBucketRequest
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.GetObjectResponse
import java.io.IOException

@Testcontainers(disabledWithoutDocker = true)
@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(FlociExtension::class)
class ProfilePicturesControllerTest : AbstractProfilePicturesControllerTest(), TestPropertyProvider {

    @Inject
    lateinit var s3: S3Client

    @BeforeEach
    fun setup() {
        s3.createBucket(CreateBucketRequest.builder().bucket(BUCKET_NAME).build())
    }

    @AfterEach
    fun cleanup() {
        s3.deleteBucket(DeleteBucketRequest.builder().bucket(BUCKET_NAME).build())
    }

    override fun getProperties(): MutableMap<String, String> =
        Floci.getProperties().toMutableMap()

    @Throws(IOException::class)
    override fun assertThatFileIsStored(key: String, expected: String) {
        val inputStream: ResponseInputStream<GetObjectResponse> = s3.getObject(
            GetObjectRequest.builder()
                .key(key)
                .bucket(BUCKET_NAME)
                .build()
        )
        assertEquals(expected, textFromFile(inputStream))
    }

    companion object {
        const val BUCKET_NAME = "micronaut-guide-object-storage"
    }
}
