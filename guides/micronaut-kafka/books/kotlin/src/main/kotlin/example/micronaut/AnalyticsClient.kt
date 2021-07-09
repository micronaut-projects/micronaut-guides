package example.micronaut

import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.Topic

@KafkaClient
interface AnalyticsClient {

    @Topic("analytics") // <1>
    fun updateAnalytics(book: Book) // <2>
}
