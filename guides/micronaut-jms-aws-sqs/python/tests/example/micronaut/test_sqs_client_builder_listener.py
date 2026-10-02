import java
from java.net import URI

from jakarta.inject import Singleton
from micronaut.context.event import BeanCreatedEvent, BeanCreatedEventListener

from .test_sqs_config import SqsConfig


AwsBasicCredentials = java.type("software.amazon.awssdk.auth.credentials.AwsBasicCredentials")
Region = java.type("software.amazon.awssdk.regions.Region")
SqsClientBuilder = java.type("software.amazon.awssdk.services.sqs.SqsClientBuilder")
StaticCredentialsProvider = java.type("software.amazon.awssdk.auth.credentials.StaticCredentialsProvider")


@Singleton  # <1>
class SqsClientBuilderListener(BeanCreatedEventListener[SqsClientBuilder]):  # <2>

    def __init__(self, sqs_config: SqsConfig):  # <3>
        self.sqs_config = sqs_config

    def onCreated(self, event: BeanCreatedEvent[SqsClientBuilder]) -> SqsClientBuilder:
        builder = event.getBean()
        return (
            builder
            .endpointOverride(URI(self.sqs_config.sqs.endpoint_override))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(
                        self.sqs_config.access_key_id,
                        self.sqs_config.secret_key,
                    )
                )
            )
            .region(Region.of(self.sqs_config.region))
        )
