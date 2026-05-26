/*
 * Copyright 2017-2026 original authors
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
package example.micronaut

import groovy.transform.CompileStatic

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@CompileStatic
@Table(name = 'products') // <1>
@Entity // <2>
class Product {

    @Id // <3>
    Long id

    @Column(nullable = false, unique = true) // <4>
    String code

    @Column(nullable = false) // <4>
    String name

    Product() {}

    Product(Long id, String code, String name) {
        this.id = id
        this.code = code
        this.name = name
    }
}
