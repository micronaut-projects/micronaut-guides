package example.micronaut.mock

import com.oracle.bmc.core.model.Instance.LifecycleState

class MockData {

    public static String instanceOcid = 'test-instance-id'
    public static LifecycleState instanceLifecycleState

    static void reset() {
        instanceOcid = 'test-instance-id'
        instanceLifecycleState = null
    }
}
