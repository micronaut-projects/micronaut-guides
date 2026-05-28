/*
 * Copyright 2017-2026 original authors
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

import io.micronaut.context.exceptions.ConfigurationException
import io.micronaut.core.io.buffer.ByteBuffer
import io.micronaut.core.io.buffer.ReferenceCounted
import io.micronaut.http.HttpRequest
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.reactor.http.client.ReactorStreamingHttpClient
import jakarta.annotation.PreDestroy
import reactor.core.publisher.Flux
import java.net.MalformedURLException
import java.net.URI

@Controller // <1>
class HomeController : AutoCloseable {

    private val reactorStreamingHttpClient: ReactorStreamingHttpClient

    init {
        val urlStr = "https://guides.micronaut.io/"
        val url = try {
            URI.create(urlStr).toURL()
        } catch (e: MalformedURLException) {
            throw ConfigurationException("malformed URL: $urlStr")
        }
        reactorStreamingHttpClient = ReactorStreamingHttpClient.create(url) // <2>
    }

    @Get // <3>
    fun download(): Flux<ByteBuffer<*>> {
        val request: HttpRequest<*> = HttpRequest.GET<Any>(DEFAULT_URI)
        return reactorStreamingHttpClient.dataStream(request).doOnNext { byteBuffer ->
            if (byteBuffer is ReferenceCounted) {
                byteBuffer.retain()
            }
        } // <4>
    }

    @PreDestroy // <5>
    override fun close() {
        reactorStreamingHttpClient.close()
    }

    companion object {
        private val DEFAULT_URI: URI = URI.create("https://guides.micronaut.io/micronaut5K.png")
    }
}
