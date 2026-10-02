import java


OpenAPIDefinition = java.type("io.swagger.v3.oas.annotations.OpenAPIDefinition")
Info = java.type("io.swagger.v3.oas.annotations.info.Info")
Server = java.type("io.swagger.v3.oas.annotations.servers.Server")


@OpenAPIDefinition(
    info=Info(
        title="micronaut-guides",
        version="1.0",
    ),
    servers=[Server(url="https://guides.micronaut.io")],
)  # <1>
class Application:
    pass
