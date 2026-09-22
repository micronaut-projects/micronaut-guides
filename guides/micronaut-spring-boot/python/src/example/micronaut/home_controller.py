from micronaut.views import View
from org.springframework.ui import Model
from org.springframework.web.bind.annotation import GetMapping, RestController


@RestController  # <1>
class HomeController:
    @GetMapping(path="/", produces="text/html")  # <2>
    @View("home")  # <3>
    def home(self, model: Model) -> str:  # <3>
        model.addAttribute(
            "message",
            "Welcome to Micronaut for Spring",
        )

        return "home"  # <4>
