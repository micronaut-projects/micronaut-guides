/*
 * Copyright 2017-2024 original authors
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
package example.micronaut;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false, transactional = false) // <1>
class ContactRepositoryTest {

    @Inject
    ContactRepository contactRepository; // <2>

    @Inject
    PhoneRepository phoneRepository;  // <3>

    @Test
    void testAssociationsQuerying() {
        String firstName = "Sergio";
        String lastName = "Sergio";
        long contactCount = contactRepository.count();
        ContactEntity e = contactRepository.save(new ContactEntity(null, firstName, lastName, null));
        assertEquals(1 + contactCount, contactRepository.count());

        Optional<ContactPreview> preview = contactRepository.findPreviewById(e.id());
        assertTrue(preview.isPresent());
        assertEquals(new ContactPreview(e.id(), firstName, lastName), preview.get());

        // Query with @Join
        Optional<ContactEntity> contactEntity = contactRepository.getById(e.id());
        assertTrue(contactEntity.isPresent());
        ContactEntity expected = new ContactEntity(contactEntity.get().id(),
                firstName,
                lastName,
                Collections.emptyList());
        assertEquals(expected, contactEntity.get());

        Optional<ContactComplete> complete = contactRepository.findCompleteById(e.id());
        assertTrue(complete.isPresent());
        assertEquals(new ContactComplete(e.id(), firstName, lastName, null), complete.get());

        String americanPhone = "+14155552671";
        String ukPhone = "+442071838750";
        long phoneCount = phoneRepository.count();
        ContactEntity contactReference = new ContactEntity(e.id(), null, null, null);
        PhoneEntity usPhoneEntity = phoneRepository.save(new PhoneEntity(null, americanPhone, contactReference));
        PhoneEntity ukPhoneEntity =phoneRepository.save(new PhoneEntity(null, ukPhone, contactReference));
        assertEquals(2 + phoneCount, phoneRepository.count());

        // Projection without join with @Query
        preview = contactRepository.findPreviewById(e.id());
        assertTrue(preview.isPresent());
        assertEquals(new ContactPreview(e.id(), firstName, lastName), preview.get());

        // findById without @Join
        contactEntity = contactRepository.findById(e.id());
        assertTrue(contactEntity.isPresent());
        assertEquals(new ContactEntity(contactEntity.get().id(), firstName, lastName, Collections.emptyList()), contactEntity.get());

        // Query with @Join
        contactEntity = contactRepository.getById(e.id());
        assertTrue(contactEntity.isPresent());
        expected = new ContactEntity(contactEntity.get().id(),
                firstName,
                lastName,
                List.of(
                        new PhoneEntity(usPhoneEntity.id(), usPhoneEntity.phone(), new ContactEntity(e.id(), e.firstName(), e.lastName(), Collections.emptyList())),
                        new PhoneEntity(ukPhoneEntity.id(), ukPhoneEntity.phone(), new ContactEntity(e.id(), e.firstName(), e.lastName(), Collections.emptyList()))));
        assertEquals(expected, contactEntity.get());

        // Projection with join with @Query
        complete = contactRepository.findCompleteById(e.id());
        assertTrue(complete.isPresent());
        assertEquals(new ContactComplete(e.id(), firstName, lastName, Set.of(americanPhone, ukPhone)), complete.get());

        //cleanup
        phoneRepository.deleteByContact(contactReference);
        contactRepository.deleteById(e.id());
        assertEquals(phoneCount, phoneRepository.count());
        assertEquals(contactCount, contactRepository.count());
    }

}