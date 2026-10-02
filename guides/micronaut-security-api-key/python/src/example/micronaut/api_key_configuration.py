from dataclasses import dataclass

from micronaut.context.annotation import EachProperty
from micronaut.serde.annotation import Serdeable


# tag::clazz[]
@Serdeable
@EachProperty("api-keys")  # <1>
@dataclass
class ApiKeyConfiguration:
    name: str
    key: str
# end::clazz[]
