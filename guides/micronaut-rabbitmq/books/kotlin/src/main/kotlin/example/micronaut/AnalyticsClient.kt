package example.micronaut

import io.micronaut.rabbitmq.annotation.Binding
import io.micronaut.rabbitmq.annotation.RabbitClient

@RabbitClient("micronaut") // <1>
interface AnalyticsClient {

    @Binding("analytics") // <2>
    fun updateAnalytics(book: Book) // <3>
}
