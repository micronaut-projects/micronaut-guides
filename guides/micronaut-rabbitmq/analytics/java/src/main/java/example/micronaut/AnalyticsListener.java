package example.micronaut;

import io.micronaut.rabbitmq.annotation.Queue;
import io.micronaut.rabbitmq.annotation.RabbitListener;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;

@Requires(notEnv = Environment.TEST) // <1>
@RabbitListener // <2>
public class AnalyticsListener {

    private final AnalyticsService analyticsService; // <3>

    public AnalyticsListener(AnalyticsService analyticsService) { // <3>
        this.analyticsService = analyticsService;
    }

    @Queue("analytics") // <4>
    public void updateAnalytics(Book book) {
        analyticsService.updateBookAnalytics(book); // <5>
    }
}
