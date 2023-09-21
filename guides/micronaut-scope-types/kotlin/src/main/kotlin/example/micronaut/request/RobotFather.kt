package example.micronaut.request

import jakarta.inject.Singleton

@Singleton
class RobotFather(private val robot: Robot) {
    fun child() = robot
}