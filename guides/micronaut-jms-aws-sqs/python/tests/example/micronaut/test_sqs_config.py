from micronaut.context.annotation import ConfigurationProperties


@ConfigurationProperties("aws")  # <1>
class SqsConfig:

    @ConfigurationProperties("services.sqs")
    class Sqs:
        endpoint_override: str

    access_key_id: str
    secret_key: str
    region: str
    sqs: Sqs = Sqs()
