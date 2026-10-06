from micronaut.http.annotation import Get


@Get("/")
def index() -> str:
    return "Hello World"


@Get("/hello/{name}")
def hello(name: str) -> dict[str, str]:
    return {"message": f"Hello {name}!"}
