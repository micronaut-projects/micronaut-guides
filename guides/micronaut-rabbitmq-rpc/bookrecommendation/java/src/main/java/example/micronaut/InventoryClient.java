package example.micronaut;

import io.micronaut.rabbitmq.annotation.Binding;
import io.micronaut.rabbitmq.annotation.RabbitClient;
import io.micronaut.rabbitmq.annotation.RabbitProperty;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@RabbitClient("micronaut") // <1>
@RabbitProperty(name = "replyTo", value = "amq.rabbitmq.reply-to") // <2>
public interface InventoryClient {

    @Binding("books.inventory") // <3>
    Mono<Boolean> stock(String isbn); // <4>

}
