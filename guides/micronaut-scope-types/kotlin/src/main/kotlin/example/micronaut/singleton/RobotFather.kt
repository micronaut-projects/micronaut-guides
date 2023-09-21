package example.micronaut.singleton

import jakarta.inject.Singleton

@Singleton // <1>
class RobotFather(private val robot: Robot) { // <2>
    fun child() = robot
}