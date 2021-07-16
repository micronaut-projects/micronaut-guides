package example.micronaut

import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.Channel
import io.micronaut.rabbitmq.connect.ChannelInitializer
import java.io.IOException
import javax.inject.Singleton

@Singleton
class ChannelPoolListener : ChannelInitializer() {

    @Throws(IOException::class)
    override fun initialize(channel: Channel) {
        channel.exchangeDeclare("micronaut", BuiltinExchangeType.DIRECT, true)
        channel.queueDeclare("analytics", true, false, false, null)
        channel.queueBind("analytics", "micronaut", "analytics")
    }
}
