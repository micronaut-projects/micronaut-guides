package example.micronaut

import com.oracle.bmc.core.ComputeClient
import com.oracle.bmc.core.model.Instance
import com.oracle.bmc.core.requests.GetInstanceRequest
import com.oracle.bmc.core.requests.InstanceActionRequest
import com.oracle.bmc.core.responses.InstanceActionResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static com.oracle.bmc.core.model.Instance.LifecycleState.Running
import static com.oracle.bmc.core.model.Instance.LifecycleState.Stopped

@Controller('/compute') // <1>
class MicronautguideController {

    private final Logger log = LoggerFactory.getLogger(getClass().name)

    private static final String START = 'START'
    private static final String STOP = 'STOP'

    private final ComputeClient computeClient

    MicronautguideController(ComputeClient computeClient) { // <2>
        this.computeClient = computeClient
    }

    @Get('/status/{ocid}') // <3>
    InstanceData status(String ocid) {
        return new InstanceData(getInstance(ocid))
    }

    @Post('/start/{ocid}') // <4>
    InstanceData start(String ocid) {
        log.info('Starting Instance: {}', ocid)

        Instance instance = getInstance(ocid)
        if (instance.lifecycleState == Stopped) {
            InstanceActionResponse response = instanceAction(ocid, START)
            log.info('Start response code: {}', response.__httpStatusCode__)
            instance = response.instance
        }
        else {
            log.info('The instance was in the incorrect state ({}) to start: {}',
                    instance.lifecycleState, ocid)
        }

        log.info('Started Instance: {}', ocid)
        return new InstanceData(instance)
    }

    @Post('/stop/{ocid}') // <5>
    InstanceData stop(String ocid) {
        log.info('Stopping Instance: {}', ocid)

        Instance instance = getInstance(ocid)
        if (instance.lifecycleState == Running) {
            InstanceActionResponse response = instanceAction(ocid, STOP)
            log.info('Stop response code: {}', response.__httpStatusCode__)
            instance = response.instance
        }
        else {
            log.info('The instance was in the incorrect state ({}) to stop: {}',
                    instance.lifecycleState, ocid)
        }

        log.info('Stopped Instance: {}', ocid)
        return new InstanceData(instance)
    }

    private InstanceActionResponse instanceAction(String ocid, String action) {
        InstanceActionRequest request = InstanceActionRequest.builder()
                .instanceId(ocid)
                .action(action)
                .build()
        return computeClient.instanceAction(request)
    }

    private Instance getInstance(String ocid) {
        GetInstanceRequest getInstanceRequest = GetInstanceRequest.builder()
                .instanceId(ocid)
                .build()
        return computeClient.getInstance(getInstanceRequest).instance
    }
}
