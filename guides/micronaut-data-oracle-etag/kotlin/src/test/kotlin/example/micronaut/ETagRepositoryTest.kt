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
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

@MicronautTest(transactional = false)
class ETagRepositoryTest {

    @Inject
    lateinit var bookRepository: ETagBookRepository

    @Inject
    lateinit var explicitBookRepository: ExplicitETagBookRepository

    @Test
    fun staleEtagPreventsAnUpdate() {
        val saved = bookRepository.save(ETagBook(title = "Initial", details = ETagBook.BookDetails(200, 10)))
        val fresh = bookRepository.findById(saved.id!!).orElseThrow() // <1>
        assertNotNull(fresh.etag)

        bookRepository.update(fresh.copy(title = "Updated")) // <2>

        val stale = fresh.copy(title = "Stale") // <3>
        assertThrows(OptimisticLockException::class.java) {
            bookRepository.update(stale)
        }
    }

    @Test
    fun changingAnExcludedFieldDoesNotChangeTheEtag() {
        val saved = explicitBookRepository.save(
            ExplicitETagBook(title = "Oracle", notes = "Initial notes")
        )
        val fresh = explicitBookRepository.findById(saved.id!!).orElseThrow()

        explicitBookRepository.update(fresh.copy(notes = "Updated notes"))
        val reloaded = explicitBookRepository.findById(fresh.id!!).orElseThrow()

        assertEquals("Updated notes", reloaded.notes)
        assertEquals(fresh.etag, reloaded.etag)
    }
}
