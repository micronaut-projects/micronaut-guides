package example.micronaut

import com.oracle.bmc.core.model.Instance
import com.oracle.bmc.core.model.Instance.LifecycleState
import groovy.transform.CompileStatic
import io.micronaut.serde.annotation.Serdeable

@CompileStatic
@Serdeable
class InstanceData {

    final String availabilityDomain
    final String compartmentOcid
    final String displayName
    final LifecycleState lifecycleState
    final String ocid
    final String region
    final Date timeCreated

    InstanceData(Instance instance) {
        availabilityDomain = instance.availabilityDomain
        compartmentOcid = instance.compartmentId
        displayName = instance.displayName
        lifecycleState = instance.lifecycleState
        ocid = instance.id
        region = instance.region
        timeCreated = instance.timeCreated
    }
}
