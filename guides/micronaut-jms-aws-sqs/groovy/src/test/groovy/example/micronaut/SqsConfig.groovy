package example.micronaut

import io.micronaut.context.annotation.ConfigurationBuilder
import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("aws") // <1>
class SqsConfig {
    String accessKeyId
    String secretKey
    String region

    @ConfigurationBuilder(configurationPrefix = "services.sqs")
    final Sqs sqs = new Sqs()

    static class Sqs {
        String endpointOverride
    }
}
