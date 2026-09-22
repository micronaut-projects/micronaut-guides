from java.io import StringWriter
from java.util import Collections
from micronaut.core.io import Writable
from micronaut.http import HttpRequest
from micronaut.runtime.event.annotation import EventListener
from micronaut.views.turbo import TurboStream, TurboStreamAction, TurboStreamRenderer
from micronaut.views.turbo.http import TurboMediaType
from micronaut.websocket import WebSocketBroadcaster, WebSocketSession
from micronaut.websocket.annotation import OnClose, OnMessage, OnOpen, ServerWebSocket

from .models.room_message import RoomMessage


# tag::clazz[]
@ServerWebSocket("/chat/{room}")  # <1>
class ChatServerWebSocket:
    def __init__(
        self,
        broadcaster: WebSocketBroadcaster,  # <2>
        turbo_stream_renderer: TurboStreamRenderer[HttpRequest],
    ):
        self.broadcaster = broadcaster
        self.turbo_stream_renderer = turbo_stream_renderer
        self.room_sessions: dict[str, set[str]] = {}

    @OnOpen  # <3>
    def onOpen(self, room: str, session: WebSocketSession) -> None:
        self.room_sessions.setdefault(room, set()).add(session.getId())

    @OnMessage  # <4>
    def onMessage(self, room: str, message: str, session: WebSocketSession) -> None:
        pass

    @OnClose  # <5>
    def onClose(self, room: str, session: WebSocketSession) -> None:
        self.room_sessions.setdefault(room, set()).discard(session.getId())

    @EventListener
    def onApplicationEvent(self, event: RoomMessage) -> None:
        self.broadcast(event)

    def broadcast(self, message: RoomMessage) -> None:
        self.turbo_stream_renderer.render(self.turbo_stream(message), None).ifPresent(
            lambda writable: self.broadcast_writable(writable, str(message.room))
        )

    def broadcast_writable(self, writable: Writable, room: str) -> None:
        text = self.writable_to_string(writable)
        if text is not None:
            self.broadcaster.broadcastAsync(
                text,
                TurboMediaType.TURBO_STREAM_TYPE,
                self.in_room(room),  # <2>
            )

    def in_room(self, room: str):
        websocket_ids = self.room_sessions.get(room, set())
        return lambda session: session.getId() in websocket_ids

    def writable_to_string(self, writable: Writable) -> str | None:
        string_writer = StringWriter()
        writable.writeTo(string_writer)
        return string_writer.toString()

    def turbo_stream(self, message: RoomMessage):
        return (
            TurboStream.builder()
            .action(TurboStreamAction.APPEND)
            .template("/messages/_message.html", Collections.singletonMap("message", message))
            .targetDomId("messages")  # <6>
        )
# end::clazz[]
