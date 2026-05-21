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

import com.oracle.bmc.Region
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName
import java.io.IOException
import java.io.InputStream
import java.io.UncheckedIOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.function.Supplier

private const val PRIVATE_KEY_PATH = "src/test/resources/key.pem"

class OciEmulatorContainer(dockerImageName: DockerImageName) : GenericContainer<OciEmulatorContainer>(dockerImageName) {

    override fun configure() {
        super.configure()
        addExposedPort(DEFAULT_PORT)
    }

    val endpoint: String
        get() = "http://127.0.0.1:${getMappedPort(DEFAULT_PORT)}"

    val compartmentId: String
        get() = "ocid1.compartment.oc1..testcompartment"

    val tenantId: String
        get() = "ocid1.tenancy.oc1..testtenancy"

    val userId: String
        get() = "ocid1.user.oc1..testuser"

    val fingerprint: String
        get() = "50:a6:c1:a1:da:71:57:dc:87:ae:90:af:9c:38:99:67"

    val privateKeySupplier: Supplier<InputStream>
        get() = Supplier {
            try {
                Files.newInputStream(Paths.get(PRIVATE_KEY_PATH))
            } catch (e: IOException) {
                throw UncheckedIOException("Unable to read test OCI private key from $PRIVATE_KEY_PATH", e)
            }
        }

    val region: Region
        get() = Region.SA_SAOPAULO_1

    companion object {
        val DEFAULT_IMAGE_NAME: DockerImageName = DockerImageName.parse("cameritelabs/oci-emulator")
        const val DEFAULT_PORT = 12000
    }
}
