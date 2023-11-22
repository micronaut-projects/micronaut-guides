package example.micronaut

class FruitDuplicateException extends RuntimeException{

    FruitDuplicateException(String name) {
        super("Fruit '" + name + "' already exists.")
    }
}
