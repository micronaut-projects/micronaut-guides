from typing import Optional

from jakarta.inject import Singleton
from micronaut.context.event import ApplicationEventPublisher

from ..entities.message import Message
from ..models.message_form import MessageForm
from ..models.room_message import RoomMessage
from ..repositories.message_repository import MessageRepository
from ..repositories.room_repository import RoomRepository
from .message_service import MessageService


# tag::clazz[]
@Singleton  # <1>
class DefaultMessageService(MessageService):
    def __init__(
        self,
        message_repository: MessageRepository,
        room_repository: RoomRepository,
        event_publisher: ApplicationEventPublisher,
    ):
        self.message_repository = message_repository
        self.room_repository = room_repository
        self.event_publisher = event_publisher

    def save(self, form: MessageForm) -> Optional[RoomMessage]:
        room = self.room_repository.findById(form.room).orElse(None)
        if room is None:
            return None

        message = self.message_repository.save(Message(content=form.content, room=room))
        room_message = RoomMessage(
            id=message.id,
            room=form.room,
            content=form.content,
            dateCreated=message.dateCreated,
        )
        self.event_publisher.publishEvent(room_message)  # <2>
        return room_message
# end::clazz[]
