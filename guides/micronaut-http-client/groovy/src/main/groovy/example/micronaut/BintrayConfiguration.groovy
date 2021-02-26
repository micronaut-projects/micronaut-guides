package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.context.annotation.Requires

@CompileStatic
@ConfigurationProperties(BintrayConfiguration.PREFIX)
@Requires(property = BintrayConfiguration.PREFIX)
class BintrayConfiguration {

    public static final String PREFIX = "bintray"
    public static final String BINTRAY_API_URL = "https://bintray.com"

    String apiversion

    String organization

    String repository

    String username

    String token

    Map<String, Object> toMap() {
        [
                apiversion: apiversion,
                organization: organization,
                repository: repository,
                username: username,
                token: token
        ] as Map<String, Object>
    }
}
