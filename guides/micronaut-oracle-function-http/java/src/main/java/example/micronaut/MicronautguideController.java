package example.micronaut;

import com.oracle.bmc.core.ComputeClient;
import com.oracle.bmc.core.model.Instance;
import com.oracle.bmc.core.requests.GetInstanceRequest;
import com.oracle.bmc.core.requests.InstanceActionRequest;
import com.oracle.bmc.core.responses.InstanceActionResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.oracle.bmc.core.model.Instance.LifecycleState.Running;
import static com.oracle.bmc.core.model.Instance.LifecycleState.Stopped;

@Controller("/compute") // <1>
public class MicronautguideController {

    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    private static final String START = "START";
    private static final String STOP = "STOP";

    private final ComputeClient computeClient;

    public MicronautguideController(ComputeClient computeClient) { // <2>
        this.computeClient = computeClient;
    }

    @Get("/status/{ocid}") // <3>
    public InstanceData status(String ocid) {
        return new InstanceData(getInstance(ocid));
    }

    @Post("/start/{ocid}") // <4>
    public InstanceData start(String ocid) {
        log.info("Starting Instance: {}", ocid);

        Instance instance = getInstance(ocid);
        if (instance.getLifecycleState() == Stopped) {
            InstanceActionResponse response = instanceAction(ocid, START);
            log.info("Start response code: {}", response.get__httpStatusCode__());
            instance = response.getInstance();
        } else {
            log.info("The instance was in the incorrect state ({}) to start: {}",
                    instance.getLifecycleState(), ocid);
        }

        log.info("Started Instance: {}", ocid);
        return new InstanceData(instance);
    }

    @Post("/stop/{ocid}") // <5>
    public InstanceData stop(String ocid) {
        log.info("Stopping Instance: {}", ocid);

        Instance instance = getInstance(ocid);
        if (instance.getLifecycleState() == Running) {
            InstanceActionResponse response = instanceAction(ocid, STOP);
            log.info("Stop response code: {}", response.get__httpStatusCode__());
            instance = response.getInstance();
        } else {
            log.info("The instance was in the incorrect state ({}) to stop: {}",
                    instance.getLifecycleState(), ocid);
        }

        log.info("Stopped Instance: {}", ocid);
        return new InstanceData(instance);
    }

    private InstanceActionResponse instanceAction(String ocid, String action) {
        InstanceActionRequest request = InstanceActionRequest.builder()
                .instanceId(ocid)
                .action(action)
                .build();
        return computeClient.instanceAction(request);
    }

    private Instance getInstance(String ocid) {
        GetInstanceRequest getInstanceRequest = GetInstanceRequest.builder()
                .instanceId(ocid)
                .build();
        return computeClient.getInstance(getInstanceRequest).getInstance();
    }
}
