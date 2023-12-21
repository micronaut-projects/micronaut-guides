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
package example.micronaut

import com.oracle.bmc.core.model.Instance
import com.oracle.bmc.core.model.Instance.LifecycleState
import io.micronaut.serde.annotation.Serdeable
import java.util.Date

@Serdeable
class InstanceData(instance: Instance) {
    val availabilityDomain: String
    val compartmentOcid: String
    val displayName: String
    val lifecycleState: LifecycleState
    val ocid: String
    val region: String
    val timeCreated: Date

    init {
        availabilityDomain = instance.availabilityDomain
        compartmentOcid = instance.compartmentId
        displayName = instance.displayName
        lifecycleState = instance.lifecycleState
        ocid = instance.id
        region = instance.region
        timeCreated = instance.timeCreated
    }
}
