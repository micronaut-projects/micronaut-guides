package example.micronaut.prototype

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/prototype") // <1>
class RobotController(
    private val robotMother: RobotMother, // <2>
    private val robotFather: RobotFather
) {

    @Get // <3>
    fun children(): List<String> {
        return listOf(robotMother.child().getSerialNumber(), robotFather.child().getSerialNumber())
    }
}