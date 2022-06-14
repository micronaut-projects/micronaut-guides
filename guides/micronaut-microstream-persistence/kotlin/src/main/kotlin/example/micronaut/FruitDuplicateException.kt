package example.micronaut

class FruitDuplicateException(name: String) : RuntimeException("Fruit '$name' already exists.")