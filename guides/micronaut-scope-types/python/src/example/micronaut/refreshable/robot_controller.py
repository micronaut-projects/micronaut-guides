from typing import Annotated

from jakarta.inject import Inject
from micronaut.http.annotation import Get

from .robot_father import RobotFather
from .robot_mother import RobotMother

father: Annotated[RobotFather, Inject]
mother: Annotated[RobotMother, Inject]


@Get("/refreshable")
def children() -> list[str]:
    return [
        father.child().serial_number,
        mother.child().serial_number,
    ]
