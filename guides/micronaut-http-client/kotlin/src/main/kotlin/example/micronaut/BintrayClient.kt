package example.micronaut

import io.reactivex.Flowable
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client

@Client(BintrayConfiguration.BINTRAY_URL) // <1>
interface BintrayClient {

    @Get("/api/\${bintray.apiversion}/repos/\${bintray.organization}/\${bintray.repository}/packages")  // <2>
    fun fetchPackages(): Flowable<BintrayPackage>  // <3>
}