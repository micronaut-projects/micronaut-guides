package example.micronaut.crypto

import io.micronaut.serde.annotation.Serdeable

@Serdeable // <1>
class BitcoinPrice(private val data: Data) {

    val price: Float
        get() = data.price

    @Serdeable // <1>
    class Data(val price: Float)
}
