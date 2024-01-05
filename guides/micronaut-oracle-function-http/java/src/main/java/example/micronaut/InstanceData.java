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
package example.micronaut;

import com.oracle.bmc.core.model.Instance;
import com.oracle.bmc.core.model.Instance.LifecycleState;
import io.micronaut.serde.annotation.Serdeable;

import java.util.Date;

@Serdeable
public class InstanceData {

    private final String availabilityDomain;
    private final String compartmentOcid;
    private final String displayName;
    private final LifecycleState lifecycleState;
    private final String ocid;
    private final String region;
    private final Date timeCreated;

    public InstanceData(Instance instance) {
        availabilityDomain = instance.getAvailabilityDomain();
        compartmentOcid = instance.getCompartmentId();
        displayName = instance.getDisplayName();
        lifecycleState = instance.getLifecycleState();
        ocid = instance.getId();
        region = instance.getRegion();
        timeCreated = instance.getTimeCreated();
    }

    public String getAvailabilityDomain() {
        return availabilityDomain;
    }

    public String getCompartmentOcid() {
        return compartmentOcid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public LifecycleState getLifecycleState() {
        return lifecycleState;
    }

    public String getOcid() {
        return ocid;
    }

    public String getRegion() {
        return region;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }
}
