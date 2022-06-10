package example.micronaut.crypto

import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import org.graalvm.compiler.replacements.nodes.BitCountNode
import io.micronaut.http.annotation.QueryValue

@Client(id = "kucoin")
interface PriceClient {

    @Get("/api/v1/market/orderbook/level1")
    fun latest(@QueryValue symbol: String): BitcoinPrice

    fun latestInUSD(): BitcoinPrice = latest("BTC-USDT")
}
