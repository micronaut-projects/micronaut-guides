package example.micronaut.crypto

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Introspected

@CompileStatic
@Introspected // <1>
class BitcoinPrice {

    private final Data data

    BitcoinPrice(Data data) {
        this.data = data
    }

    float getPrice() {
        return data.price
    }

    @CompileStatic
    @Introspected // <1>
    static class Data {

        final float price

        Data(float price) {
            this.price = price
        }
    }
}
