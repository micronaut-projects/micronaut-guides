package example.micronaut

import io.micronaut.rabbitmq.annotation.Binding
import io.micronaut.rabbitmq.annotation.RabbitClient
import io.micronaut.rabbitmq.annotation.RabbitProperty
import org.reactivestreams.Publisher

@RabbitClient("micronaut") // <1>
@RabbitProperty(name = "replyTo", value = "amq.rabbitmq.reply-to") // <2>
interface CatalogueClient {

    @Binding("books.catalogue") // <3>
    fun findAll(data: ByteArray?): Publisher<List<Book>> // <4>
}
