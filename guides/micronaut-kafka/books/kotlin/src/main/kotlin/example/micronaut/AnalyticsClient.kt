package example.micronaut

import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.Topic
import org.reactivestreams.Publisher
import io.micronaut.core.async.annotation.SingleResult

@KafkaClient
interface AnalyticsClient {

    @Topic("analytics") // <1>
    @SingleResult
    fun updateAnalytics(book: Book) : Publisher<Book> // <2>
}
