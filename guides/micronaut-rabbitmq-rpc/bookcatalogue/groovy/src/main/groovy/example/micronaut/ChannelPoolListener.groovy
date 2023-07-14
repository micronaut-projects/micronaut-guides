package example.micronaut

import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.Channel
import groovy.transform.CompileStatic
import io.micronaut.rabbitmq.connect.ChannelInitializer
import jakarta.inject.Singleton

@Singleton
@CompileStatic
class ChannelPoolListener extends ChannelInitializer {

    @Override
    void initialize(Channel channel, String name) throws IOException {
        channel.exchangeDeclare('micronaut', BuiltinExchangeType.DIRECT, true) // <1>

        channel.queueDeclare('inventory', true, false, false, null) // <2>
        channel.queueBind('inventory', 'micronaut', 'books.inventory') // <3>

        channel.queueDeclare('catalogue', true, false, false, null) // <4>
        channel.queueBind('catalogue', 'micronaut', 'books.catalogue') // <5>
    }
}
