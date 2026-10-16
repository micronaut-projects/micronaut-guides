from typing import Annotated

from jakarta.inject import Inject
from micronaut.http.annotation import Get

from .robot_father import RobotFather
from .robot_mother import RobotMother

# tag::pkg[]
# end::pkg[]
# tag::imports[]
# tag::controller[]
father: Annotated[RobotFather, Inject]  # <1>
mother: Annotated[RobotMother, Inject]  # <2>
# end::controller[]
# end::imports[]


# tag::clazz[]
@Get("/singleton")  # <3>
def children() -> list[str]:
    return [
        father.child().serial_number,
        mother.child().serial_number,
    ]
# end::clazz[]
