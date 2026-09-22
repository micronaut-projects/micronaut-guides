from abc import ABC, abstractmethod
from typing import Optional

from ..models.message_form import MessageForm
from ..models.room_message import RoomMessage


# tag::clazz[]
class MessageService(ABC):  # <1>
    @abstractmethod
    def save(self, form: MessageForm) -> Optional[RoomMessage]: ...  # <2>
# end::clazz[]
