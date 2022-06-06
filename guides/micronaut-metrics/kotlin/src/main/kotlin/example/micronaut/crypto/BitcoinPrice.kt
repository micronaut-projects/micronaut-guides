package example.micronaut.crypto

import io.micronaut.core.annotation.Introspected

@Introspected
class BitcoinPrice(private val data: Data) {
    val price: Float
        get() = data.price

    @Introspected
    class Data(val price: Float)
}
