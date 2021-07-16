package example.micronaut;

import io.micronaut.rabbitmq.annotation.Binding;
import io.micronaut.rabbitmq.annotation.RabbitClient;
import io.micronaut.rabbitmq.annotation.RabbitProperty;
import reactor.core.publisher.Flux;
import org.reactivestreams.Publisher;
import java.util.List;

@RabbitClient("micronaut") // <1>
@RabbitProperty(name = "replyTo", value = "amq.rabbitmq.reply-to") // <2>
public interface CatalogueClient {

    @Binding("books.catalogue") // <3>
    Publisher<List<Book>> findAll(byte[] data); // <4>

}
