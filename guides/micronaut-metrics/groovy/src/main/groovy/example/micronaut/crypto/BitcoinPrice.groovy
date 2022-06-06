package example.micronaut.crypto

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Introspected

@CompileStatic
@Introspected
class BitcoinPrice {

    private final Data data

    BitcoinPrice(Data data) {
        this.data = data
    }

    float getPrice() {
        return data.price
    }

    @CompileStatic
    @Introspected
    static class Data {

        final float price

        Data(float price) {
            this.price = price
        }
    }
}
