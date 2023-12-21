/*
 * Copyright 2017-2023 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
