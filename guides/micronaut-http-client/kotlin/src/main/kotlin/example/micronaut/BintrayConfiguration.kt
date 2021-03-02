package example.micronaut

import example.micronaut.BintrayConfiguration.Companion.PREFIX
import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties(PREFIX)
class BintrayConfiguration {
    companion object {
        const val PREFIX = "bintray"
        const val BINTRAY_URL = "https://bintray.com"
    }

    var apiversion: String? = null
    var organization: String? = null
    var repository: String? = null
    var username: String? = null
    var token: String? = null

    fun toMap(): Map<String, Any> {
        val m = HashMap<String, Any>()
        apiversion?.let {
            m["apiversion"] = it
        }
        organization?.let {
            m["organization"] = it
        }
        repository?.let {
            m["repository"] = it
        }
        username?.let {
            m["username"] = it
        }
        token?.let {
            m["token"] = it
        }
        return m
    }
}

