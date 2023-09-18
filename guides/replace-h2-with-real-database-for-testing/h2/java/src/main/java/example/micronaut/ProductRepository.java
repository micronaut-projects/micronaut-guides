package example.micronaut;

//tag::clazz[]
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository // <1>
interface ProductRepository extends JpaRepository<Product, Long> { // <2>
//end::clazz[]
//tag::method[]
    default void createProductIfNotExists(Product product) {
        createProductIfNotExists(product.getId(), product.getCode(), product.getName());
    }

    @Query(
            value = "insert into products(id, code, name) values(:id, :code, :name) ON CONFLICT DO NOTHING",
            nativeQuery = true
    ) // <3>
    void createProductIfNotExists(Long id, String code, String name);
//end::method[]
//tag::close[]
}
//end::close[]