package example.micronaut

import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.Topic
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment

@Requires(notEnv = [Environment.TEST]) // <1>
@KafkaListener // <2>
class AnalyticsListener(private val analyticsService: AnalyticsService) { // <3>

    @Topic("analytics") // <4>
    fun updateAnalytics(book: Book) = analyticsService.updateBookAnalytics(book) // <5>
}
