package example.micronaut

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.reactivex.Flowable
import io.reactivex.Maybe

@Controller("/bintray") // <1>
class BintrayController(private val bintrayLowLevelClient : BintrayLowLevelClient,  // <2>
                        private val bintrayClient: BintrayClient) {

    @Get("/packages-lowlevel")  // <3>
    fun packagesWithLowLevelClient(): Maybe<List<BintrayPackage>> {  // <4>
        return bintrayLowLevelClient.fetchPackages()
    }

    @Get(uri = "/packages", produces = arrayOf(MediaType.APPLICATION_JSON_STREAM))  // <5>
    internal fun packages(): Flowable<BintrayPackage> {  // <6>
        return bintrayClient.fetchPackages()
    }
}