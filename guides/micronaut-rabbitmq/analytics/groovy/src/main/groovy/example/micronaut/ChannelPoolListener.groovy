package example.micronaut

import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.Channel
import groovy.transform.CompileStatic
import io.micronaut.rabbitmq.connect.ChannelInitializer

import javax.inject.Singleton

@CompileStatic
@Singleton
class ChannelPoolListener extends ChannelInitializer {

    @Override
    void initialize(Channel channel) throws IOException {
        channel.exchangeDeclare('micronaut', BuiltinExchangeType.DIRECT, true) // <1>
        channel.queueDeclare('analytics', true, false, false, null) // <2>
        channel.queueBind('analytics', 'micronaut', 'analytics') // <3>
    }
}
