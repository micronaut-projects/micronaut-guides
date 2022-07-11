package example.micronaut

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication=false) // <1>
class HelloServiceSpec extends Specification {

    @Inject // <2>
    HelloRepositoryInMemory helloRepository

    @Inject // <2>
    HelloService helloService



    def cleanup() {
        helloRepository.clearRepository()
    }

    def 'when hello is in repository should return hello in language'() {
        given:
        var polishHello = "Czesc!"
        helloRepository.putHelloInLanguage("polish", polishHello)

        when:
        String result = helloService.sayHello("polish")

        then:
        result == polishHello // <3>
    }

    def 'when hello is not in repository should return default hello'() {
        given:
        String result = helloService.sayHello("polish")

        expect:
        result == "Hello!" // <3>
    }

}
