package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.rabbitmq.annotation.Binding
import io.micronaut.rabbitmq.annotation.RabbitClient
import io.micronaut.rabbitmq.annotation.RabbitProperty
import reactor.core.publisher.Mono

@CompileStatic
@RabbitClient('micronaut') // <1>
@RabbitProperty(name = 'replyTo', value = 'amq.rabbitmq.reply-to') // <2>
interface InventoryClient {

    @Binding('books.inventory') // <3>
    Mono<Boolean> stock(String isbn) // <4>
}
