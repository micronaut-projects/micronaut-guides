package example.micronaut.request

import io.micronaut.http.HttpRequest
import io.micronaut.runtime.http.scope.RequestAware
import io.micronaut.runtime.http.scope.RequestScope

@RequestScope // <1>
class Robot : RequestAware { // <2>
    private var serialNumber: String? = null

    override fun setRequest(request: HttpRequest<*>?) {
        this.serialNumber = request!!.headers.get("UUID")
    }

    fun getSerialNumber(): String {
        return serialNumber!!
    }
}