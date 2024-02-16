/*
 * Copyright 2017-2024 original authors
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
package example.micronaut.mock

import com.oracle.bmc.auth.BasicAuthenticationDetailsProvider
import com.oracle.bmc.core.ComputeClient
import com.oracle.bmc.core.model.Instance
import com.oracle.bmc.core.requests.GetInstanceRequest
import com.oracle.bmc.core.requests.InstanceActionRequest
import com.oracle.bmc.core.responses.GetInstanceResponse
import com.oracle.bmc.core.responses.InstanceActionResponse
import io.micronaut.context.annotation.Replaces
import jakarta.inject.Singleton

@Singleton
@Replaces(ComputeClient::class) // <1>
class MockComputeClient(provider: BasicAuthenticationDetailsProvider) // <3>
    : ComputeClient(provider) { // <2>

    override fun getInstance(request: GetInstanceRequest) =
            GetInstanceResponse.builder()
                    .instance(buildInstance(MockData.instanceLifecycleState!!))
                    .build()

    override fun instanceAction(request: InstanceActionRequest): InstanceActionResponse {
        val lifecycleState = if ("START" == request.action)
            Instance.LifecycleState.Starting else
            Instance.LifecycleState.Stopping
        return InstanceActionResponse.builder()
                .instance(buildInstance(lifecycleState))
                .build()
    }

    private fun buildInstance(lifecycleState: Instance.LifecycleState) =
            Instance.builder()
                    .id(MockData.instanceOcid)
                    .lifecycleState(lifecycleState)
                    .build()
}
