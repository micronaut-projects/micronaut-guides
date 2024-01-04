package example.micronaut.singleton
/*
//tag::pkg[]
package example.micronaut.singleton
//end::pkg[]
*/

//tag::imports[]
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
//end::imports[]

//tag::controller[]
@Controller("/singleton") // <1>
//end::controller[]
//tag::clazz[]
class RobotController(
    private val robotFather: RobotFather, // <2>
    private val robotMother: RobotMother // <3>
) {

    @Get // <4>
    fun children(): List<String> {
        return listOf(robotMother.child().getSerialNumber(), robotFather.child().getSerialNumber())
    }
}
//end::clazz[]