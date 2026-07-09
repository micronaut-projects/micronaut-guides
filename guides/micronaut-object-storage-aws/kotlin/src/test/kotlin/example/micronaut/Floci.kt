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

import io.floci.testcontainers.FlociContainer
import org.testcontainers.utility.DockerImageName

object Floci {
    private val FLOCI_IMAGE: DockerImageName = DockerImageName.parse("floci/floci:1.5.18")
    private var floci: FlociContainer? = null

    private fun getFloci(): FlociContainer {
        init()
        return floci!!
    }

    private fun getEndpoint(): String =
        getFloci().endpoint

    private fun secretAccessKey(): String =
        getFloci().secretKey

    private fun getRegion(): String =
        getFloci().region

    private fun accessKeyId(): String =
        getFloci().accessKey

    fun getProperties(): Map<String, String> = mapOf(
        "aws.accessKeyId" to accessKeyId(),
        "aws.secretKey" to secretAccessKey(),
        "aws.region" to getRegion(),
        "aws.services.s3.endpoint-override" to getEndpoint(),
        "aws.services.s3.path-style-access-enabled" to "true"
    )

    fun init() {
        if (floci == null) {
            floci = FlociContainer(FLOCI_IMAGE)
            floci!!.start()
        }
    }

    fun close() {
        floci?.close()
        floci = null
    }
}
