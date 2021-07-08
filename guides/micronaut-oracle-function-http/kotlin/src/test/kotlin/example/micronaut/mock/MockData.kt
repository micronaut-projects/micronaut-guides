package example.micronaut.mock

import com.oracle.bmc.core.model.Instance.LifecycleState

object MockData {

    var instanceOcid = "test-instance-id"
    var instanceLifecycleState: LifecycleState? = null

    fun reset() {
        instanceOcid = "test-instance-id"
        instanceLifecycleState = null
    }
}
