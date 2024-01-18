package example.micronaut

import io.micronaut.context.annotation.ConfigurationBuilder
import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("aws") // <1>
class SqsConfig {
    var accessKeyId: String? = null
    var secretKey: String? = null
    var region: String? = null

    @ConfigurationBuilder(configurationPrefix = "services.sqs")
    val sqs: Sqs = Sqs()

    class Sqs {
        var endpointOverride: String? = null
    }
}