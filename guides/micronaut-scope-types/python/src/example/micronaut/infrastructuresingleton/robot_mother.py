from jakarta.inject import Singleton

from .robot import Robot


@Singleton  # <1>
class RobotMother:
    def __init__(self, robot: Robot):  # <2>
        self._robot = robot

    def child(self) -> Robot:
        return self._robot
