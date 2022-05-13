package example.micronaut

import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.spock.annotation.MicronautTest

@MicronautTest
class FruitControllerSpec extends BaseMicroStreamSpec {

    void "empty database has no fruit"() {
        expect:
        fruitClient.list().collect().size() == 0
    }

    void "check interaction with the controller"() {
        when:
        def response = fruitClient.create(new FruitCommand("banana", null))

        then:
        response.status == HttpStatus.CREATED

        when:
        def banana = response.getBody().get()
        def fruitList = fruitClient.list()

        then:
        fruitList.size() == 1
        fruitList[0].name == banana.getName()
        fruitList[0].description == null

        when:
        response = fruitClient.create(new FruitCommand("apple", "Keeps the doctor away"))

        then:
        response.status == HttpStatus.CREATED

        when:
        fruitList = fruitClient.list()

        then:
        fruitList.any { it.description == "Keeps the doctor away" }

        when:
        fruitClient.update(new FruitCommand("banana", "Yellow and curved"))
        fruitList = fruitClient.list()

        then:
        fruitList*.description.toSet() == ["Keeps the doctor away", "Yellow and curved"] as Set<String>
    }
}
