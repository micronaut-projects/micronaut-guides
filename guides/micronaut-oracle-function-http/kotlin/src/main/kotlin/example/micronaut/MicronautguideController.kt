package example.micronaut

import com.oracle.bmc.core.ComputeClient
import com.oracle.bmc.core.model.Instance.LifecycleState.Running
import com.oracle.bmc.core.model.Instance.LifecycleState.Stopped
import com.oracle.bmc.core.requests.GetInstanceRequest
import com.oracle.bmc.core.requests.InstanceActionRequest
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import org.slf4j.LoggerFactory

@Controller("/compute") // <1>
class MicronautguideController(private val computeClient: ComputeClient) { // <2>

    private val log = LoggerFactory.getLogger(javaClass.name)

    @Get("/status/{ocid}") // <3>
    fun status(ocid: String) = InstanceData(getInstance(ocid))

    @Post("/start/{ocid}") // <4>
    fun start(ocid: String): InstanceData {
        log.info("Starting Instance: {}", ocid)

        var instance = getInstance(ocid)
        if (instance.lifecycleState == Stopped) {
            val response = instanceAction(ocid, START)
            log.info("Start response code: {}", response.__httpStatusCode__)
            instance = response.instance
        } else {
            log.info("The instance was in the incorrect state ({}) to start: {}",
                    instance.lifecycleState, ocid)
        }

        log.info("Started Instance: {}", ocid)
        return InstanceData(instance)
    }

    @Post("/stop/{ocid}") // <5>
    fun stop(ocid: String): InstanceData {
        log.info("Stopping Instance: {}", ocid)

        var instance = getInstance(ocid)
        if (instance.lifecycleState == Running) {
            val response = instanceAction(ocid, STOP)
            log.info("Stop response code: {}", response.__httpStatusCode__)
            instance = response.instance
        } else {
            log.info("The instance was in the incorrect state ({}) to stop: {}",
                    instance.lifecycleState, ocid)
        }

        log.info("Stopped Instance: {}", ocid)
        return InstanceData(instance)
    }

    private fun instanceAction(ocid: String, action: String) =
            computeClient.instanceAction(InstanceActionRequest.builder()
                    .instanceId(ocid)
                    .action(action)
                    .build())

    private fun getInstance(ocid: String) =
            computeClient.getInstance(GetInstanceRequest.builder()
                    .instanceId(ocid)
                    .build()).instance

    companion object {
        private const val START = "START"
        private const val STOP = "STOP"
    }
}
