package example.micronaut;

import com.oracle.bmc.core.model.Instance;
import com.oracle.bmc.core.model.Instance.LifecycleState;
import io.micronaut.core.annotation.Introspected;

import java.util.Date;

@Introspected
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
