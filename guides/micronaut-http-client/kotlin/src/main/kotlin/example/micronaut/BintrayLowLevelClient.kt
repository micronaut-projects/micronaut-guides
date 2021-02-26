package example.micronaut

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriTemplate
import io.reactivex.Maybe
import javax.inject.Singleton

@Singleton // <1>
class BintrayLowLevelClient(@param:Client(BintrayConfiguration.BINTRAY_URL) private val httpClient: RxHttpClient, // <2>
                            bintrayConfiguration: BintrayConfiguration) { // <3>
    private val uri: String

    init {
        val path = "/api/{apiversion}/repos/{organization}/{repository}/packages"
        uri = UriTemplate.of(path).expand(bintrayConfiguration.toMap())
    }

    internal fun fetchPackages(): Maybe<List<BintrayPackage>> {
        val req = HttpRequest.GET<Any>(uri)  // <4>
        val flowable = httpClient.retrieve(req, Argument.listOf(BintrayPackage::class.java))  // <5>
        return flowable.firstElement() as Maybe<List<BintrayPackage>>  // <6>
    }

}