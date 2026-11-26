from micronaut.http.annotation import Controller, Get

from .robot_father import RobotFather
from .robot_mother import RobotMother


@Controller("/singleton")  # <1>
class RobotController:
    def __init__(self, father: RobotFather, mother: RobotMother):  # <2>
        self.father = father
        self.mother = mother

    @Get  # <3>
    def children(self) -> list[str]:
        return [
            self.father.child().serial_number,
            self.mother.child().serial_number,
        ]
