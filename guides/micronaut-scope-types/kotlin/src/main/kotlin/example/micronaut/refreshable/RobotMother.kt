package example.micronaut.refreshable

import jakarta.inject.Singleton

@Singleton
class RobotMother(private val robot: Robot) {
    fun child() = robot
}