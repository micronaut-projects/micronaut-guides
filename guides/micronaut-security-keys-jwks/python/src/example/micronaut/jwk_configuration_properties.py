from dataclasses import dataclass
from typing import Annotated

from jakarta.validation.constraints import NotBlank
from micronaut.context.annotation import ConfigurationProperties

from .jwk_configuration import JwkConfiguration


# tag::clazz[]
@ConfigurationProperties("jwk")  # <1>
@dataclass
class JwkConfigurationProperties(JwkConfiguration):
    primary: Annotated[str, NotBlank]  # <2>
    secondary: Annotated[str, NotBlank]  # <2>
# end::clazz[]
