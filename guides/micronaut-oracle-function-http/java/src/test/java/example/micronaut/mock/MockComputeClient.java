/*
 * Copyright 2017-2023 original authors
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
package example.micronaut.mock;

import com.oracle.bmc.auth.BasicAuthenticationDetailsProvider;
import com.oracle.bmc.core.ComputeClient;
import com.oracle.bmc.core.model.Instance;
import com.oracle.bmc.core.model.Instance.LifecycleState;
import com.oracle.bmc.core.requests.GetInstanceRequest;
import com.oracle.bmc.core.requests.InstanceActionRequest;
import com.oracle.bmc.core.responses.GetInstanceResponse;
import com.oracle.bmc.core.responses.InstanceActionResponse;
import io.micronaut.context.annotation.Replaces;
import jakarta.inject.Singleton;

import static com.oracle.bmc.core.model.Instance.LifecycleState.Starting;
import static com.oracle.bmc.core.model.Instance.LifecycleState.Stopping;

@Singleton
@Replaces(ComputeClient.class) // <1>
public class MockComputeClient extends ComputeClient { // <2>

    public MockComputeClient(BasicAuthenticationDetailsProvider provider) { // <3>
        super(provider);
    }

    @Override
    public GetInstanceResponse getInstance(GetInstanceRequest request) {
        return GetInstanceResponse.builder()
                .instance(buildInstance(MockData.instanceLifecycleState))
                .build();
    }

    @Override
    public InstanceActionResponse instanceAction(InstanceActionRequest request) {
        LifecycleState lifecycleState = "START".equals(request.getAction()) ? Starting : Stopping;
        return InstanceActionResponse.builder()
                .instance(buildInstance(lifecycleState))
                .build();
    }

    private Instance buildInstance(LifecycleState lifecycleState) {
        return Instance.builder()
                .id(MockData.instanceOcid)
                .lifecycleState(lifecycleState)
                .build();
    }
}
