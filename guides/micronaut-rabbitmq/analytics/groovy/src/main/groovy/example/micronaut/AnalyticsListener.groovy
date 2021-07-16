package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.rabbitmq.annotation.Queue
import io.micronaut.rabbitmq.annotation.RabbitListener
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment

@CompileStatic
@Requires(notEnv = Environment.TEST) // <1>
@RabbitListener
class AnalyticsListener {

    private final AnalyticsService analyticsService // <3>

    AnalyticsListener(AnalyticsService analyticsService) { // <3>
        this.analyticsService = analyticsService
    }

    @Queue('analytics') // <4>
    void updateAnalytics(Book book) {
        analyticsService.updateBookAnalytics(book) // <5>
    }
}
