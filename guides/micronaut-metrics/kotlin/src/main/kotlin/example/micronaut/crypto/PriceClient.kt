package example.micronaut.crypto

@Client(id = "kucoin") // <1>
abstract class PriceClient {
    @Get("/api/v1/market/orderbook/level1")
    abstract fun latest(@QueryValue symbol: String): BitcoinPrice

    fun latestInUSD(): BitcoinPrice {
        return latest("BTC-USDT")
    }
}