package example.micronaut;

import io.micronaut.rabbitmq.annotation.Binding;
import io.micronaut.rabbitmq.annotation.RabbitClient;

@RabbitClient("micronaut") // <1>
public interface AnalyticsClient {

    @Binding("analytics") // <2>
    void updateAnalytics(Book book); // <3>
}
