package example.micronaut.prototype

import io.micronaut.context.annotation.Prototype
import java.util.UUID

@Prototype // <1>
class Robot {
    private val serialNumber: String = UUID.randomUUID().toString()

    fun getSerialNumber(): String {
        return serialNumber
    }
}