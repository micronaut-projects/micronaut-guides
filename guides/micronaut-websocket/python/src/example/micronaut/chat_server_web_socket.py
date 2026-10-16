from micronaut.websocket import WebSocketBroadcaster, WebSocketSession
from micronaut.websocket.annotation import OnClose, OnMessage, OnOpen, ServerWebSocket


@ServerWebSocket("/ws/chat/{topic}/{username}")  # <1>
class ChatServerWebSocket:

    def __init__(self, broadcaster: WebSocketBroadcaster):  # <2>
        self.broadcaster = broadcaster

    @OnOpen  # <3>
    def on_open(self, topic: str, username: str, session: WebSocketSession) -> None:
        if topic == "all":  # <4>
            message = f"[{username}] Now making announcements!"
        else:
            message = f"[{username}] Joined {topic}!"
        self.broadcaster.broadcastSync(message, self.is_valid(topic))

    @OnMessage  # <5>
    def on_message(
        self,
        topic: str,
        username: str,
        message: str,
        session: WebSocketSession,
    ) -> None:
        self.broadcaster.broadcastSync(f"[{username}] {message}", self.is_valid(topic))

    @OnClose  # <6>
    def on_close(self, topic: str, username: str, session: WebSocketSession) -> None:
        self.broadcaster.broadcastSync(f"[{username}] Leaving {topic}!", self.is_valid(topic))

    def is_valid(self, topic: str):  # <7>
        return lambda session: (
            topic == "all"
            or "all" == self.session_topic(session)
            or topic.lower() == self.session_topic(session).lower()
        )

    def session_topic(self, session: WebSocketSession) -> str:
        value = session.getUriVariables().asMap().get("topic")
        if value is None:
            return ""
        return str(value)
