package example.micronaut

import jakarta.inject.Singleton

@Singleton // <1>
class BookRepositoryImpl : BookRepository {

    override fun findAll() = listOf(
            Book("1491950358", "Building Microservices"),
            Book("1680502395", "Release It!"),
            Book("0321601912", "Continuous Delivery:"))
}
