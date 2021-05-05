package example.micronaut

import grails.gorm.multitenancy.Tenants
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.context.event.StartupEvent

import javax.inject.Inject
import javax.inject.Singleton
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment

@Requires(notEnv = Environment.TEST) // <1>
@Singleton // <2>
class Bootstrap implements ApplicationEventListener<StartupEvent> { // <3>

    @Inject // <4>
    BookService bookService

    @Override
    void onApplicationEvent(StartupEvent event) {
        Tenants.withId("sherlock") { // <5>
            bookService.save('Sherlock diary')
        }
        Tenants.withId("watson") { // <6>
            bookService.save('Watson diary')
        }
    }
}
