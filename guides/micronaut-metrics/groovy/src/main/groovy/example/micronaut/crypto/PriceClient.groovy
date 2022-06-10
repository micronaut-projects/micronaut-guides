package example.micronaut.crypto

import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.annotation.QueryValue

@Client(id = 'kucoin') // <1>
abstract class PriceClient {

    @Get("/api/v1/market/orderbook/level1")
    abstract BitcoinPrice latest(@QueryValue String symbol)

    BitcoinPrice latestInUSD() {
        latest("BTC-USDT")
    }
}
