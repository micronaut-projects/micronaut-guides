package example.micronaut.crypto

import io.micronaut.core.annotation.Introspected

@Introspected // <1>
class BitcoinPrice(private val data: Data) {

    val price: Float
        get() = data.price

    @Introspected // <1>
    class Data(val price: Float)
}
