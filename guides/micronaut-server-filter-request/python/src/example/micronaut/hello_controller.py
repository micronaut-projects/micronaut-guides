from micronaut.http.annotation import Controller, Get


@Controller  # <1>
class HelloController:

    @Get  # <2>
    def index(self) -> dict[str, str]:
        return {"message": "Hello World"}  # <3>
