package example.micronaut;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.event.BeanCreatedEvent;
import io.micronaut.context.event.BeanCreatedEventListener;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.awssdk.v2_2.AwsSdkTelemetry;
import io.opentelemetry.instrumentation.awssdk.v2_2.AwsSdkTelemetryBuilder;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.client.builder.SdkClientBuilder;
//import io.opentelemetry.instrumentation.awssdk.v2_2.AwsSdkTracing;

/**
 * Configures X-Ray Tracing Interceptor for all sdk client builders.
 * @see <a href="https://aws.amazon.com/blogs/developer/x-ray-support-for-the-aws-sdk-for-java-v2/">X-Ray support for the AWS SDK for Java 2</a>
 *
 * @author Pavol Gressa
 * @since 3.2.0
 */
@Requires(classes = SdkClientBuilder.class)
@Singleton
public class SdkClientBuilderListener implements BeanCreatedEventListener<SdkClientBuilder<?, ?>> {
    private static final Logger LOG = LoggerFactory.getLogger(SdkClientBuilderListener.class);


    private final OpenTelemetry openTelemetry;

    public SdkClientBuilderListener(OpenTelemetry openTelemetry) {
        this.openTelemetry = openTelemetry;
    }

    /**
     * Add an OpenTelemetry execution interceptor to {@link SdkClientBuilder}.
     *
     * @param event bean created event
     * @return sdk client builder
     */
    @Override
    public SdkClientBuilder<?, ?> onCreated(BeanCreatedEvent<SdkClientBuilder<?, ?>> event) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Registering OpenTelemetry tracing interceptor to {}", event.getBean().getClass().getSimpleName());
        }
        return event.getBean().overrideConfiguration(builder ->
                builder.addExecutionInterceptor(AwsSdkTelemetry.create(openTelemetry).newExecutionInterceptor()));
    }
}
