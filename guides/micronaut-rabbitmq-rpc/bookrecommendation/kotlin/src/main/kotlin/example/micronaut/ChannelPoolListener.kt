/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.micronaut

import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.Channel
import io.micronaut.rabbitmq.connect.ChannelInitializer
import jakarta.inject.Singleton
import java.io.IOException

@Singleton
class ChannelPoolListener : ChannelInitializer() {

    @Throws(IOException::class)
    override fun initialize(channel: Channel, name: String) {
        channel.exchangeDeclare("micronaut", BuiltinExchangeType.DIRECT, true) // <1>
        channel.queueDeclare("inventory", true, false, false, null) // <2>
        channel.queueBind("inventory", "micronaut", "books.inventory") // <3>
        channel.queueDeclare("catalogue", true, false, false, null) // <4>
        channel.queueBind("catalogue", "micronaut", "books.catalogue") // <5>
    }
}
