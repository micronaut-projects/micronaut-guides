package example.micronaut.singleton
/*
//tag::pkg[]
package example.micronaut.singleton
//end::pkg[]
*/

//tag::clazz[]
import jakarta.inject.Singleton
import java.util.UUID

@Singleton // <1>
class Robot {
    private val serialNumber: String = UUID.randomUUID().toString()

    fun getSerialNumber(): String {
        return serialNumber
    }
}
//end::clazz[]