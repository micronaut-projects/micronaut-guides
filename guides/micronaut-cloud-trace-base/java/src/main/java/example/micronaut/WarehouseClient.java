package example.micronaut;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.tracing.annotation.ContinueSpan;
import io.micronaut.tracing.annotation.SpanTag;
import io.opentelemetry.instrumentation.annotations.SpanAttribute;
import io.opentelemetry.instrumentation.annotations.WithSpan;

import java.util.Map;

@Client("/warehouse") // <1>
public interface WarehouseClient {

    @Post("/order")
    @WithSpan
    void order(@SpanTag("warehouse.order") Map<String, ?> json);

    @Get("/count")
    @ContinueSpan
    int getItemCount(@QueryValue String store, @SpanAttribute @QueryValue int upc);

}
