package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.reactivex.Flowable
import io.reactivex.Maybe

@CompileStatic
@Controller("/bintray") // <1>
class BintrayController {
    private final BintrayLowLevelClient bintrayLowLevelClient

    private final BintrayClient bintrayClient

    BintrayController(BintrayLowLevelClient bintrayLowLevelClient, // <2>
                             BintrayClient bintrayClient) {
        this.bintrayLowLevelClient = bintrayLowLevelClient
        this.bintrayClient = bintrayClient
    }

    @Get("/packages-lowlevel") // <3>
    Maybe<List<BintrayPackage>> packagesWithLowLevelClient() { // <4>
        return bintrayLowLevelClient.fetchPackages()
    }

    @Get(uri = "/packages", produces = MediaType.APPLICATION_JSON_STREAM) // <5>
    Flowable<BintrayPackage> packages() { // <6>
        return bintrayClient.fetchPackages()
    }
}
