package example.micronaut.crypto

import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client

@Client(id = "kucoin") // <1>
abstract class PriceClient {

    @Get("/api/v1/market/orderbook/level1")
    abstract fun latest(@QueryValue symbol: String): BitcoinPrice

    fun latestInUSD(): BitcoinPrice = latest("BTC-USDT")
}
