from dataclasses import dataclass

from micronaut.context.annotation import ConfigurationProperties, Requires


@Requires(property="app.hosted-domain")
@ConfigurationProperties("app")
@dataclass
class ApplicationConfiguration:
    hosted_domain: str
