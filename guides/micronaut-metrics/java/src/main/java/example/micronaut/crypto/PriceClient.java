package example.micronaut.crypto;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.client.annotation.Client;

@Client(id = "kucoin") // <1>
public abstract class PriceClient {

    @Get("/api/v1/market/orderbook/level1")
    abstract BitcoinPrice latest(@QueryValue String symbol);

    public BitcoinPrice latestInUSD() {
        return latest("BTC-USDT");
    }
}
