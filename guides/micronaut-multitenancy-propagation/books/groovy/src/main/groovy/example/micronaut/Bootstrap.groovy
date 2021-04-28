package example.micronaut

import grails.gorm.multitenancy.Tenants
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.context.event.StartupEvent

import javax.inject.Inject
import javax.inject.Singleton

@Singleton // <1>
class Bootstrap implements ApplicationEventListener<StartupEvent> { // <2>

    @Inject // <3>
    BookService bookService

    @Override
    void onApplicationEvent(StartupEvent event) {

        Tenants.withId("sherlock") { // <4>
            bookService.save('Sherlock diary')
        }
        Tenants.withId("watson") { // <4>
            bookService.save('Watson diary')
        }
    }
}
