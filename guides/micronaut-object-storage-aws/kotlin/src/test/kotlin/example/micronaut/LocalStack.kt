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

import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.utility.DockerImageName

object LocalStack {
    private const val TAG = "4.14.0"
    private const val IMAGE = "localstack/localstack"
    private var localstack: LocalStackContainer? = null

    private fun getLocalStack(): LocalStackContainer {
        init()
        return localstack!!
    }

    private fun getEndpoint(): String =
        getLocalStack().getEndpointOverride(LocalStackContainer.Service.S3).toString()

    private fun secretAccessKey(): String =
        getLocalStack().secretKey

    private fun getRegion(): String =
        getLocalStack().region

    private fun accessKeyId(): String =
        getLocalStack().accessKey

    fun getProperties(): Map<String, String> = mapOf(
        "aws.accessKeyId" to accessKeyId(),
        "aws.secretKey" to secretAccessKey(),
        "aws.region" to getRegion(),
        "aws.services.s3.endpoint-override" to getEndpoint()
    )

    fun init() {
        if (localstack == null) {
            localstack = LocalStackContainer(DockerImageName.parse("$IMAGE:$TAG"))
                .withServices(LocalStackContainer.Service.S3)
            localstack!!.start()
        }
    }

    fun close() {
        localstack?.close()
        localstack = null
    }
}
