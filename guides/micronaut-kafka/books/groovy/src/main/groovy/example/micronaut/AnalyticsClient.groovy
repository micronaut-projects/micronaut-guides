package example.micronaut

import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.Topic
import io.reactivex.Single

@KafkaClient
interface AnalyticsClient {

    @Topic('analytics') // <1>
    Single<Book> updateAnalytics(Book book) // <2>
}
