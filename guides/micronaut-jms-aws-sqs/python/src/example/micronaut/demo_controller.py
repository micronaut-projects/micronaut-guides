from micronaut.http import HttpStatus
from micronaut.http.annotation import Controller, Post, Status

from .demo_producer import DemoProducer


@Controller  # <1>
class DemoController:

    def __init__(self, demo_producer: DemoProducer):  # <2>
        self.demo_producer = demo_producer

    @Post("/demo")  # <3>
    @Status(HttpStatus.NO_CONTENT)
    def publish_demo_messages(self) -> None:
        self.demo_producer.send("Demo message body")  # <4>
