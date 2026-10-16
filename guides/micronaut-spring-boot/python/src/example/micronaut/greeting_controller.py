from typing import Annotated

from jakarta.validation.constraints import Pattern
from org.springframework.http import HttpStatus, ResponseEntity
from org.springframework.util import LinkedMultiValueMap
from org.springframework.web.bind.annotation import (
    DeleteMapping,
    GetMapping,
    PostMapping,
    RequestBody,
    RequestParam,
    ResponseStatus,
    RestController,
)

from .greeting import Greeting
from .greeting_service import GreetingService


@RestController  # <1>
class GreetingController:
    def __init__(self, greeting_service: GreetingService):  # <2>
        self.greeting_service = greeting_service

    @GetMapping("/greeting")  # <3>
    def greeting(
        self,
        name: Annotated[
            str,
            RequestParam(value="name", defaultValue="World"),
            Pattern(regexp="\\D+"),
        ],
    ) -> Greeting:  # <4>
        return self.greeting_service.greeting(name)

    @PostMapping("/greeting")  # <5>
    def greeting_by_post(self, greeting: Annotated[Greeting, RequestBody]) -> Greeting:  # <6>
        return self.greeting_service.greeting(greeting.content)

    @DeleteMapping("/greeting")  # <7>
    def delete_greeting(self) -> ResponseEntity:  # <8>
        headers = LinkedMultiValueMap()
        headers.add("Foo", "Bar")
        return ResponseEntity(headers, HttpStatus.NO_CONTENT)  # <8>

    @GetMapping("/greeting-status")  # <3>
    @ResponseStatus(code=HttpStatus.CREATED)  # <9>
    def greeting_with_status(
        self,
        name: Annotated[
            str,
            RequestParam(value="name", defaultValue="World"),
            Pattern(regexp="\\D+"),
        ],
    ) -> Greeting:
        return self.greeting_service.greeting(name)
