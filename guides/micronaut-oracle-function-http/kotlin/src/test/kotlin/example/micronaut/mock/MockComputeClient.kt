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
