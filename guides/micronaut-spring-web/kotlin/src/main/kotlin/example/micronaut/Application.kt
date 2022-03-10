package example.micronaut

import io.micronaut.runtime.Micronaut.*
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
object Application {

    @JvmStatic
    fun main(args: Array<String>) {    
        build()
            .args(*args)
            .packages("example.micronaut")
            .start()
    }
}
