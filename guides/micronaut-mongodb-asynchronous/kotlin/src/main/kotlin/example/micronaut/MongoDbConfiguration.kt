package example.micronaut

import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.core.naming.Named

@ConfigurationProperties("db") // <1>
interface MongoDbConfiguration : Named {

    val collection: String
}
