package example.micronaut.crypto

import groovy.transform.CompileStatic
import io.micronaut.serde.annotation.Serdeable

@CompileStatic
@Serdeable // <1>
class BitcoinPrice {

    private final Data data

    BitcoinPrice(Data data) {
        this.data = data
    }

    float getPrice() {
        return data.price
    }

    @CompileStatic
    @Serdeable // <1>
    static class Data {

        final float price

        Data(float price) {
            this.price = price
        }
    }
}
