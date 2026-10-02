from jakarta.inject import Singleton


@Singleton
class MessageService:
    def compose(self) -> str:
        return "Hello World"
