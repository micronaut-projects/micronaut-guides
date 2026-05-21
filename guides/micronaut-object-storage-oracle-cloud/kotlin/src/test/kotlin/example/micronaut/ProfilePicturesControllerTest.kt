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

import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider
import com.oracle.bmc.objectstorage.ObjectStorage
import com.oracle.bmc.objectstorage.ObjectStorageClient
import com.oracle.bmc.objectstorage.model.CreateBucketDetails
import com.oracle.bmc.objectstorage.requests.CreateBucketRequest
import com.oracle.bmc.objectstorage.requests.DeleteBucketRequest
import com.oracle.bmc.objectstorage.requests.GetObjectRequest
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Requires
import io.micronaut.context.event.BeanCreatedEvent
import io.micronaut.context.event.BeanCreatedEventListener
import io.micronaut.core.annotation.NonNull
import io.micronaut.objectstorage.oraclecloud.OracleCloudStorageConfiguration
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.IOException

private const val SPEC_NAME = "ProfilePicturesControllerTest"

@Testcontainers(disabledWithoutDocker = true)
@MicronautTest
@Property(name = "spec.name", value = SPEC_NAME)
class ProfilePicturesControllerTest : AbstractProfilePicturesControllerTest() {

    @Inject
    lateinit var client: ObjectStorage

    @Inject
    lateinit var configuration: OracleCloudStorageConfiguration

    @BeforeEach
    fun beforeEach() {
        val bucketDetails = CreateBucketDetails.builder()
            .compartmentId(ociEmulator.compartmentId)
            .name(configuration.bucket)
            .build()

        client.createBucket(
            CreateBucketRequest.builder()
                .namespaceName(configuration.namespace)
                .createBucketDetails(bucketDetails)
                .build()
        )
    }

    @AfterEach
    fun afterEach() {
        client.deleteBucket(
            DeleteBucketRequest.builder()
                .namespaceName(configuration.namespace)
                .bucketName(configuration.bucket)
                .build()
        )
    }

    @Throws(IOException::class)
    override fun assertThatFileIsStored(key: String, expected: String) {
        val response = client.getObject(
            GetObjectRequest.builder()
                .bucketName(configuration.bucket)
                .namespaceName(configuration.namespace)
                .objectName(key)
                .build()
        )

        assertEquals(expected, textFromFile(response.inputStream))
    }

    @Singleton
    @Requires(property = "spec.name", value = SPEC_NAME)
    class ObjectStorageListener : BeanCreatedEventListener<ObjectStorageClient> {

        override fun onCreated(@NonNull event: BeanCreatedEvent<ObjectStorageClient>): ObjectStorageClient {
            val client = event.bean
            client.setEndpoint(ociEmulator.endpoint)
            return client
        }
    }

    @Factory
    @Requires(property = "spec.name", value = SPEC_NAME)
    class OciEmulatorAuthenticationFactory {

        @Bean
        @Singleton
        fun authenticationDetailsProvider(): SimpleAuthenticationDetailsProvider =
            SimpleAuthenticationDetailsProvider.builder()
                .tenantId(ociEmulator.tenantId)
                .userId(ociEmulator.userId)
                .fingerprint(ociEmulator.fingerprint)
                .privateKeySupplier(ociEmulator.privateKeySupplier)
                .region(ociEmulator.region)
                .build()
    }

    companion object {
        val ociEmulator = OciEmulatorContainer(OciEmulatorContainer.DEFAULT_IMAGE_NAME)

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            ociEmulator.start()
        }

        @JvmStatic
        @AfterAll
        fun afterAll() {
            ociEmulator.stop()
        }
    }
}
