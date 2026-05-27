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

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@MicronautTest(startApplication = false, transactional = false) // <1>
class ContactRepositoryTest {

    @Inject
    lateinit var contactRepository: ContactRepository // <2>

    @Inject
    lateinit var phoneRepository: PhoneRepository // <3>

    @Test
    fun testAssociationsQuerying() {
        val firstName = "Sergio"
        val lastName = "Sergio"
        val contactCount = contactRepository.count()
        val e = contactRepository.save(ContactEntity(null, firstName, lastName, null))
        val contactId = e.id!!
        assertEquals(1 + contactCount, contactRepository.count())

        var preview = contactRepository.findPreviewById(contactId)
        assertTrue(preview.isPresent)
        assertEquals(ContactPreview(contactId, firstName, lastName), preview.get())

        // Query with @Join
        var contactEntity = contactRepository.getById(contactId)
        assertTrue(contactEntity.isPresent)
        var expected = ContactEntity(contactEntity.get().id, firstName, lastName, emptyList())
        assertEquals(expected, contactEntity.get())

        var complete = contactRepository.findCompleteById(contactId)
        assertTrue(complete.isPresent)
        assertEquals(ContactComplete(contactId, firstName, lastName, null), complete.get())

        val americanPhone = "+14155552671"
        val ukPhone = "+442071838750"
        val phoneCount = phoneRepository.count()
        val contactReference = ContactEntity(contactId, null, null, null)
        val usPhoneEntity = phoneRepository.save(PhoneEntity(null, americanPhone, contactReference))
        val ukPhoneEntity = phoneRepository.save(PhoneEntity(null, ukPhone, contactReference))
        assertEquals(2 + phoneCount, phoneRepository.count())

        // Projection without join with @Query
        preview = contactRepository.findPreviewById(contactId)
        assertTrue(preview.isPresent)
        assertEquals(ContactPreview(contactId, firstName, lastName), preview.get())

        // findById without @Join
        contactEntity = contactRepository.findById(contactId)
        assertTrue(contactEntity.isPresent)
        assertEquals(ContactEntity(contactEntity.get().id, firstName, lastName, emptyList()), contactEntity.get())

        // Query with @Join
        contactEntity = contactRepository.getById(contactId)
        assertTrue(contactEntity.isPresent)
        expected = ContactEntity(
            contactEntity.get().id,
            firstName,
            lastName,
            listOf(
                PhoneEntity(
                    usPhoneEntity.id,
                    usPhoneEntity.phone,
                    ContactEntity(contactId, e.firstName, e.lastName, emptyList())
                ),
                PhoneEntity(
                    ukPhoneEntity.id,
                    ukPhoneEntity.phone,
                    ContactEntity(contactId, e.firstName, e.lastName, emptyList())
                )
            )
        )
        assertEquals(expected, contactEntity.get())

        // Projection with join with @Query
        complete = contactRepository.findCompleteById(contactId)
        assertTrue(complete.isPresent)
        assertEquals(ContactComplete(contactId, firstName, lastName, setOf(americanPhone, ukPhone)), complete.get())

        // cleanup
        phoneRepository.deleteByContact(contactReference)
        contactRepository.deleteById(contactId)
        assertEquals(phoneCount, phoneRepository.count())
        assertEquals(contactCount, contactRepository.count())
    }
}
