package example.micronaut

import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.Channel
import io.micronaut.rabbitmq.connect.ChannelInitializer
import java.io.IOException
import jakarta.inject.Singleton

@Singleton
class ChannelPoolListener : ChannelInitializer() {

    @Throws(IOException::class)
    override fun initialize(channel: Channel) {
        channel.exchangeDeclare("micronaut", BuiltinExchangeType.DIRECT, true) // <1>
        channel.queueDeclare("analytics", true, false, false, null) // <2>
        channel.queueBind("analytics", "micronaut", "analytics") // <3>
    }
}
