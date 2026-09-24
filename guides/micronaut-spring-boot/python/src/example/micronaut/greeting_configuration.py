from org.springframework.boot.context.properties import ConfigurationProperties
from org.springframework.stereotype import Component


@Component  # <1>
@ConfigurationProperties("greeting")  # <2>
class GreetingConfiguration:
    template: str = "Hello, %s!"
