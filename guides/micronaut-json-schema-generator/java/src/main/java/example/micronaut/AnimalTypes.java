package example.micronaut;

import com.example.animal.*;

public enum AnimalTypes {
    DOG(Dog.class),
    CAT(Cat.class),
    FISH(Fish.class);

    AnimalTypes(Class<? extends Animal> animalClass) {
    }

    static AnimalTypes of(String animalClass) {
        return switch (animalClass) {
            case "Dog" -> DOG;
            case "Cat" -> CAT;
            case "Fish" -> FISH;
            default -> throw new IllegalArgumentException("Unknown animal type: " + animalClass);
        };
    }
}
