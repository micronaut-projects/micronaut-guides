from swagger.v3.oas.annotations import OpenAPIDefinition
from swagger.v3.oas.annotations.info import Info
from swagger.v3.oas.annotations.servers import Server


@OpenAPIDefinition(
    info=Info(
        title="micronaut-guides",
        version="1.0",
    ),
    servers=[Server(url="https://guides.micronaut.io")],
)  # <1>
class Application:
    pass
