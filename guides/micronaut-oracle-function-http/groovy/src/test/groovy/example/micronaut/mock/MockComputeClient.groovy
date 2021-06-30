package example.micronaut.mock

import com.oracle.bmc.auth.BasicAuthenticationDetailsProvider
import com.oracle.bmc.core.ComputeClient
import com.oracle.bmc.core.model.Instance
import com.oracle.bmc.core.model.Instance.LifecycleState
import com.oracle.bmc.core.requests.GetInstanceRequest
import com.oracle.bmc.core.requests.InstanceActionRequest
import com.oracle.bmc.core.responses.GetInstanceResponse
import com.oracle.bmc.core.responses.InstanceActionResponse
import io.micronaut.context.annotation.Replaces

import javax.inject.Singleton

import static com.oracle.bmc.core.model.Instance.LifecycleState.Starting
import static com.oracle.bmc.core.model.Instance.LifecycleState.Stopping

@Singleton
@Replaces(ComputeClient) // <1>
class MockComputeClient extends ComputeClient { // <2>

    MockComputeClient(BasicAuthenticationDetailsProvider provider) { // <3>
        super(provider)
    }

    @Override
    GetInstanceResponse getInstance(GetInstanceRequest request) {
        return GetInstanceResponse.builder()
                .instance(buildInstance(MockData.instanceLifecycleState))
                .build()
    }

    @Override
    InstanceActionResponse instanceAction(InstanceActionRequest request) {
        LifecycleState lifecycleState = 'START' == request.getAction() ? Starting : Stopping
        return InstanceActionResponse.builder()
                .instance(buildInstance(lifecycleState))
                .build()
    }

    private Instance buildInstance(LifecycleState lifecycleState) {
        return Instance.builder()
                .id(MockData.instanceOcid)
                .lifecycleState(lifecycleState)
                .build()
    }
}
