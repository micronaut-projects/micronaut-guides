package example.micronaut.mock;

import com.oracle.bmc.core.model.Instance.LifecycleState;

public class MockData {

    public static String instanceOcid = "test-instance-id";
    public static LifecycleState instanceLifecycleState;

    public static void reset() {
        instanceOcid = "test-instance-id";
        instanceLifecycleState = null;
    }
}
