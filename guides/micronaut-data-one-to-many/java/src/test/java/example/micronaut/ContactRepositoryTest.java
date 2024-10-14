package example.micronaut;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false, transactional = false)
class ContactRepositoryTest {

    @Inject
    ContactRepository contactRepository;

    @Inject
    PhoneRepository phoneRepository;

    @Test
    void testSaveContact() {
        long contactCount = contactRepository.count();
        ContactEntity e = contactRepository.save(new ContactEntity(null, "Sergio", "del Amo", null));
        assertEquals(1 + contactCount, contactRepository.count());

        Optional<ContactPreview> preview = contactRepository.findPreviewById(e.id());
        assertTrue(preview.isPresent());
        assertEquals(new ContactPreview(e.id(), "Sergio", "del Amo"), preview.get());

        Optional<ContactComplete> complete = contactRepository.findCompleteById(e.id());
        assertTrue(complete.isPresent());
        assertEquals(new ContactComplete(e.id(), "Sergio", "del Amo", null), complete.get());

        // Add a phone
        String americanPhone = "+14155552671";
        String ukPhone = "+442071838750";
        long phoneCount = phoneRepository.count();
        ContactEntity contactReference = new ContactEntity(e.id(), null, null, null);
        phoneRepository.save(new PhoneEntity(null, americanPhone, contactReference));
        phoneRepository.save(new PhoneEntity(null, ukPhone, contactReference));
        assertEquals(2 + phoneCount, phoneRepository.count());

        preview = contactRepository.findPreviewById(e.id());
        assertTrue(preview.isPresent());
        assertEquals(new ContactPreview(e.id(), "Sergio", "del Amo"), preview.get());

        complete = contactRepository.findCompleteById(e.id());
        assertTrue(complete.isPresent());
        assertEquals(new ContactComplete(e.id(), "Sergio", "del Amo", Set.of(americanPhone, ukPhone)), complete.get());

        //cleanup
        phoneRepository.deleteByContact(contactReference);
        contactRepository.deleteById(e.id());
        assertEquals(phoneCount, phoneRepository.count());
        assertEquals(contactCount, contactRepository.count());
    }

}