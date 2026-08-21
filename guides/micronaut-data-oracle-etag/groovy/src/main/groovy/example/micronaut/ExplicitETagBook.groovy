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
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.sql.ETagValue
import io.micronaut.data.annotation.sql.GeneratedETag

// tag::explicit-etag[]
@CompileStatic
@MappedEntity('explicit_etag_book')
class ExplicitETagBook {
    @Id
    @GeneratedValue
    @ETagValue
    Long id
    @ETagValue
    String title
    String notes
    @GeneratedETag(function = 'SYS_ROW_ETAG')
    String etag

    ExplicitETagBook() {
    }

    ExplicitETagBook(Long id, String title, String notes, String etag) {
        this.id = id
        this.title = title
        this.notes = notes
        this.etag = etag
    }
}
// end::explicit-etag[]
