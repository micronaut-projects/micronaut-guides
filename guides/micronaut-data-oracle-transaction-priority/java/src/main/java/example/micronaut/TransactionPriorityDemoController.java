package example.micronaut;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

@ExecuteOn(TaskExecutors.BLOCKING)
@Controller("/demo")
public class TransactionPriorityDemoController {

    private final TransactionPriorityDemo demo;

    TransactionPriorityDemoController(TransactionPriorityDemo demo) {
        this.demo = demo;
    }

    @Post("/reset")
    public TransactionPriorityDemo.DemoSnapshot reset() {
        return demo.reset();
    }

    @Post("/low")
    public HttpResponse<TransactionPriorityDemo.StartResult> low(@QueryValue(defaultValue = "20") long holdSeconds) {
        TransactionPriorityDemo.StartResult result = demo.startLow(holdSeconds);
        return result.started() ? HttpResponse.status(HttpStatus.ACCEPTED).body(result) : HttpResponse.status(HttpStatus.CONFLICT).body(result);
    }

    @Post("/high")
    public HttpResponse<TransactionPriorityDemo.StartResult> high() {
        TransactionPriorityDemo.StartResult result = demo.startHigh();
        return result.started() ? HttpResponse.status(HttpStatus.ACCEPTED).body(result) : HttpResponse.status(HttpStatus.CONFLICT).body(result);
    }

    @Get
    public TransactionPriorityDemo.DemoSnapshot status() {
        return demo.snapshot();
    }
}
