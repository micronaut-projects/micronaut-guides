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

import io.micronaut.context.BeanContext
import io.micronaut.objectstorage.aws.AwsS3Operations
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@MicronautTest(startApplication = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AwsS3OperationsTest : TestPropertyProvider {

    @Inject
    lateinit var beanContext: BeanContext

    @Test
    fun onlyABeanOfTypeAwsS3OperationsExists() {
        assertDoesNotThrow { beanContext.getBean(AwsS3Operations::class.java) }
        assertEquals(1, beanContext.getBeansOfType(AwsS3Operations::class.java).size)
    }

    override fun getProperties(): MutableMap<String, String> = mutableMapOf(
        "aws.accessKeyId" to "test",
        "aws.secretKey" to "test",
        "aws.region" to "us-east-1"
    )
}
