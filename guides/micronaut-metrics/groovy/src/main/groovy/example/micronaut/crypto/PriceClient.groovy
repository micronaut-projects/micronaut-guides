package example.micronaut.crypto

import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.annotation.QueryValue

@Client(id = 'kucoin')
interface PriceClient {

    @Get("/api/v1/market/orderbook/level1")
    BitcoinPrice latest(@QueryValue String symbol);

    default BitcoinPrice latestInUSD() {
        return latest("BTC-USDT");
    }
}
