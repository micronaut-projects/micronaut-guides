package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.rabbitmq.annotation.Binding
import io.micronaut.rabbitmq.annotation.RabbitClient
import io.micronaut.rabbitmq.annotation.RabbitProperty
import org.reactivestreams.Publisher

@CompileStatic
@RabbitClient('micronaut') // <1>
@RabbitProperty(name = 'replyTo', value = 'amq.rabbitmq.reply-to') // <2>
interface CatalogueClient {

    @Binding('books.catalogue') // <3>
    Publisher<List<Book>> findAll(byte[] data) // <4>
}
