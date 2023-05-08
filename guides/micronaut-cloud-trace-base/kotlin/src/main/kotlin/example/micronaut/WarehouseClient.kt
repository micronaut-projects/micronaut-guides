package example.micronaut

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client
import io.micronaut.tracing.annotation.ContinueSpan
import io.micronaut.tracing.annotation.SpanTag
import io.opentelemetry.instrumentation.annotations.SpanAttribute
import io.opentelemetry.instrumentation.annotations.WithSpan

@Client("/warehouse") // <1>
interface WarehouseClient {

    @Post("/order")
    @WithSpan
    fun order(@SpanTag("warehouse.order") json: Map<String, Any>) : HttpResponse<Any>

    @Get("/count")
    @ContinueSpan
    fun getItemCount(@QueryValue store: String, @SpanAttribute @QueryValue upc: Int) : Int

}