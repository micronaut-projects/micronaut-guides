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

import io.micronaut.data.exceptions.OptimisticLockException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(transactional = false)
class OptimisticLockingSpec extends Specification {

    @Inject
    BookRepository bookRepository

    @Inject
    ArticleRepository articleRepository

    void 'stale etag prevents an update'() {
        when:
        def saved = bookRepository.save(new Book(null, 'Initial', new Book.BookDetails(200, 10), null))
        def fresh = bookRepository.findById(saved.id).orElseThrow() // <1>

        then:
        fresh.etag != null

        when:
        bookRepository.update(new Book(fresh.id, 'Updated', fresh.details, fresh.etag)) // <2>
        bookRepository.update(new Book(fresh.id, 'Stale', fresh.details, fresh.etag)) // <3>

        then:
        thrown(OptimisticLockException)
    }

    void 'changing an excluded field does not change the etag'() {
        given:
        def saved = articleRepository.save(new Article(null, 'Oracle', 'Initial notes', null))
        def fresh = articleRepository.findById(saved.id).orElseThrow()

        when:
        articleRepository.update(new Article(fresh.id, fresh.title, 'Updated notes', fresh.etag))
        def reloaded = articleRepository.findById(fresh.id).orElseThrow()

        then:
        reloaded.notes == 'Updated notes'
        reloaded.etag == fresh.etag
    }
}
