package example.micronaut;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;

@Requires(notEnv = Environment.TEST) // <1>
@KafkaListener // <2>
public class AnalyticsListener {

    private final AnalyticsService analyticsService; // <3>

    public AnalyticsListener(AnalyticsService analyticsService) { // <3>
        this.analyticsService = analyticsService;
    }

    @Topic("analytics") // <4>
    public void updateAnalytics(Book book) {
        analyticsService.updateBookAnalytics(book); // <5>
    }
}
