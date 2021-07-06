package example.micronaut

import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.Topic

@KafkaClient
interface AnalyticsClient {

    @Topic("analytics")
    fun updateAnalytics(book: Book)
}
