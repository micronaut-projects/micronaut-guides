package example.micronaut

import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import io.micronaut.rabbitmq.annotation.Queue
import io.micronaut.rabbitmq.annotation.RabbitListener

@Requires(notEnv = [Environment.TEST]) // <1>
@RabbitListener // <2>
class AnalyticsListener(private val analyticsService: AnalyticsService) { // <3>

    @Queue("analytics") // <4>
    fun updateAnalytics(book: Book) =
        analyticsService.updateBookAnalytics(book) // <5>
}
