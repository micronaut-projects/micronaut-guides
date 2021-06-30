package example.micronaut

import com.oracle.bmc.core.model.Instance.LifecycleState
import example.micronaut.mock.MockData
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.oraclecloud.function.http.test.FnHttpTest
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification

import static com.oracle.bmc.core.model.Instance.LifecycleState.Running
import static com.oracle.bmc.core.model.Instance.LifecycleState.Stopped
import static io.micronaut.http.HttpStatus.OK

@MicronautTest
class MicronautguideControllerSpec extends Specification {

    private static final List<Class<?>> SHARED_CLASSES = [MockData, LifecycleState] // <1>

    void 'test status'() {
        given:
        String instanceOcid = UUID.randomUUID()
        MockData.instanceOcid = instanceOcid // <2>
        MockData.instanceLifecycleState = Running

        when:
        HttpResponse<String> response = FnHttpTest.invoke( // <3>
                HttpRequest.GET("/compute/status/$instanceOcid"),
                SHARED_CLASSES)

        then:
        response.status() == OK
        response.body() == '{"lifecycleState":"Running","ocid":"' + instanceOcid + '"}' // <4>
    }

    void 'test start'() {
        given:
        String instanceOcid = UUID.randomUUID()
        MockData.instanceOcid = instanceOcid
        MockData.instanceLifecycleState = Stopped

        when:
        HttpResponse<String> response = FnHttpTest.invoke(
                HttpRequest.POST("/compute/start/$instanceOcid", null),
                SHARED_CLASSES)

        then:
        response.status() == OK
        response.body() == '{"lifecycleState":"Starting","ocid":"' + instanceOcid + '"}'
    }

    void 'test stop'() {
        given:
        String instanceOcid = UUID.randomUUID()
        MockData.instanceOcid = instanceOcid
        MockData.instanceLifecycleState = Running

        when:
        HttpResponse<String> response = FnHttpTest.invoke(
                HttpRequest.POST("/compute/stop/$instanceOcid", null),
                SHARED_CLASSES)

        then:
        response.status() == OK
        response.body() == '{"lifecycleState":"Stopping","ocid":"' + instanceOcid + '"}'
    }

    void cleanup() {
        MockData.reset()
    }
}
