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
