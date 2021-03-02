package example.micronaut;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Flowable;

@Client(BintrayConfiguration.BINTRAY_API_URL) // <1>
public interface BintrayClient {

    @Get("/api/${bintray.apiversion}/repos/${bintray.organization}/${bintray.repository}/packages") // <2>
    Flowable<BintrayPackage> fetchPackages(); // <3>
}
