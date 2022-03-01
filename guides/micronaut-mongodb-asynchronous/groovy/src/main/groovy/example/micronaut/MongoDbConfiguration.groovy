package example.micronaut

import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.naming.Named

@ConfigurationProperties('db') // <1>
interface MongoDbConfiguration extends Named {

    @NonNull
    String getCollection()
}
