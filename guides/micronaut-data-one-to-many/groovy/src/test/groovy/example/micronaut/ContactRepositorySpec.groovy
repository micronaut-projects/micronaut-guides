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

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false, transactional = false) // <1>
class ContactRepositorySpec extends Specification {

    @Inject
    ContactRepository contactRepository // <2>

    @Inject
    PhoneRepository phoneRepository // <3>

    void 'test associations querying'() {
        given:
        String firstName = 'Sergio'
        String lastName = 'Sergio'
        long contactCount = contactRepository.count()

        when:
        ContactEntity e = contactRepository.save(new ContactEntity(null, firstName, lastName, null))

        then:
        contactRepository.count() == 1 + contactCount

        when:
        Optional<ContactPreview> preview = contactRepository.findPreviewById(e.id)

        then:
        preview.present
        preview.get() == new ContactPreview(e.id, firstName, lastName)

        when: 'query with @Join'
        Optional<ContactEntity> contactEntity = contactRepository.getById(e.id)

        then:
        contactEntity.present
        contactEntity.get() == new ContactEntity(contactEntity.get().id, firstName, lastName, [])

        when:
        Optional<ContactComplete> complete = contactRepository.findCompleteById(e.id)

        then:
        complete.present
        complete.get() == new ContactComplete(e.id, firstName, lastName, null)

        when:
        String americanPhone = '+14155552671'
        String ukPhone = '+442071838750'
        long phoneCount = phoneRepository.count()
        ContactEntity contactReference = new ContactEntity(e.id, null, null, null)
        PhoneEntity usPhoneEntity = phoneRepository.save(new PhoneEntity(null, americanPhone, contactReference))
        PhoneEntity ukPhoneEntity = phoneRepository.save(new PhoneEntity(null, ukPhone, contactReference))

        then:
        phoneRepository.count() == 2 + phoneCount

        when: 'projection without join with @Query'
        preview = contactRepository.findPreviewById(e.id)

        then:
        preview.present
        preview.get() == new ContactPreview(e.id, firstName, lastName)

        when: 'findById without @Join'
        contactEntity = contactRepository.findById(e.id)

        then:
        contactEntity.present
        contactEntity.get() == new ContactEntity(contactEntity.get().id, firstName, lastName, null)

        when: 'query with @Join'
        contactEntity = contactRepository.getById(e.id)

        then:
        contactEntity.present
        contactEntity.get() == new ContactEntity(contactEntity.get().id,
                firstName,
                lastName,
                [
                        new PhoneEntity(usPhoneEntity.id, usPhoneEntity.phone, new ContactEntity(e.id, e.firstName, e.lastName, [])),
                        new PhoneEntity(ukPhoneEntity.id, ukPhoneEntity.phone, new ContactEntity(e.id, e.firstName, e.lastName, []))
                ])

        when: 'projection with join with @Query'
        complete = contactRepository.findCompleteById(e.id)

        then:
        complete.present
        complete.get() == new ContactComplete(e.id, firstName, lastName, [americanPhone, ukPhone] as Set)

        cleanup:
        phoneRepository.deleteByContact(contactReference)
        contactRepository.deleteById(e.id)
        assert phoneRepository.count() == phoneCount
        assert contactRepository.count() == contactCount
    }
}
