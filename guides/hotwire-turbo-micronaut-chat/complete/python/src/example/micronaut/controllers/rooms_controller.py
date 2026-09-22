from ..entities.room import Room
from ..repositories.room_repository import RoomRepository

ROOM = "room"
ROOMS = "rooms"


# tag::clazz[]
def room_model(room: Room | None) -> dict[str, Room] | None:
    return None if room is None else {ROOM: room}


def room_with_messages(
    id: int,
    room_repository: RoomRepository,
) -> Room | None:
    return room_repository.getById(id).orElse(None)
# end::clazz[]
