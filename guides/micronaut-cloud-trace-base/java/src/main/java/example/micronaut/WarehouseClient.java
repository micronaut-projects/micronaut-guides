package example.micronaut;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.tracing.annotation.ContinueSpan;
import io.micronaut.tracing.annotation.NewSpan;
import io.micronaut.tracing.annotation.SpanTag;

import java.util.Map;

@Client("/warehouse") // <1>
public interface WarehouseClient {

    @Get("/count")
    @ContinueSpan // <2>
    int getItemCount(@QueryValue String store, @SpanTag @QueryValue int upc);

    @Post("/order")
    @NewSpan
    void order(@SpanTag("warehouse.order") Map<String, ?> json);

}
