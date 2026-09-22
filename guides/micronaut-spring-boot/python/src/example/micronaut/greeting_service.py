from typing import Annotated

from jakarta.validation.constraints import Pattern
from org.springframework.stereotype import Service
from org.springframework.validation.annotation import Validated

from .greeting import Greeting
from .greeting_configuration import GreetingConfiguration


@Service  # <1>
@Validated  # <2>
class GreetingService:
    def __init__(self, greeting_configuration: GreetingConfiguration):  # <3>
        self.greeting_configuration = greeting_configuration
        self.counter = 0
        self.last_greeting: Greeting | None = None

    def greeting(self, name: Annotated[str, Pattern(regexp="\\D+")]) -> Greeting:  # <4>
        self.counter += 1
        greeting = Greeting(
            self.counter,
            self.greeting_configuration.template % name,
        )
        self.last_greeting = greeting
        return greeting

    def get_last_greeting(self) -> Greeting | None:
        return self.last_greeting
