from jakarta.inject import Singleton

from .robot import Robot


@Singleton  # <1>
class RobotFather:
    def __init__(self, robot: Robot):  # <2>
        self._robot = robot

    def child(self) -> Robot:
        return self._robot
