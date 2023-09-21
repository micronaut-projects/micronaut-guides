package example.micronaut.refreshable

import io.micronaut.runtime.context.scope.Refreshable
import java.util.UUID

@Refreshable // <1>
class Robot {
    private val serialNumber: String = UUID.randomUUID().toString()

    fun getSerialNumber(): String {
        return serialNumber
    }
}