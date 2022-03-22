package example.micronaut;

import example.micronaut.domain.Owner;
import example.micronaut.domain.Pet;
import example.micronaut.domain.PetType;
import example.micronaut.repositories.OwnerRepository;
import example.micronaut.repositories.PetRepository;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.context.event.StartupEvent;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@Singleton // <1>
class Bootstrap implements ApplicationEventListener<StartupEvent> { // <2>
    private static final Logger LOG = LoggerFactory.getLogger(Bootstrap.class);

    private final OwnerRepository ownerRepository;
    private final PetRepository petRepository;

    Bootstrap(OwnerRepository ownerRepository,
              PetRepository petRepository) { // <3>
        this.ownerRepository = ownerRepository;
        this.petRepository = petRepository;
    }

    @Override
    public void onApplicationEvent(StartupEvent event) { // <2>
        if (LOG.isInfoEnabled()) {
            LOG.info("Populating data");
        }

        Owner fred = new Owner("Fred");
        fred.setAge(45);
        Owner barney = new Owner("Barney");
        barney.setAge(40);
        ownerRepository.saveAll(Arrays.asList(fred, barney));

        Pet dino = new Pet("Dino", fred);
        Pet bp = new Pet("Baby Puss", fred);
        bp.setType(PetType.CAT);
        Pet hoppy = new Pet("Hoppy", barney);

        petRepository.saveAll(Arrays.asList(dino, bp, hoppy));
    }
}
