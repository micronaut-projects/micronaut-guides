//tag::clazz1[]
package example.micronaut

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.Version

@MappedEntity // <1>
class Person(
    val name: String,
//end::clazz1[]
//tag::nullableage[]
    val age: Int?
//end::nullableage[]
/*
//tag::ageint[]
    val age: Int
//end::ageint[]
*/
//tag::clazz2[]
) {

    @Id // <2>
    @GeneratedValue // <3>
    var id: Long? = null

    @Version // <4>
    var version: Long? = null
}
//end::clazz2[]
