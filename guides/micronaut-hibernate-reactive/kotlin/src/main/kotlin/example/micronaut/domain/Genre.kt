package example.micronaut.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
@Table(name = "genre")
class Genre(@Id
            @GeneratedValue(strategy = GenerationType.AUTO)
            @Column(name = "id", nullable = false, updatable = false)
            val id: Long?,

            @Column(name = "name", nullable = false)
            var name: String
) {

    @JsonIgnore
    @OneToMany(mappedBy = "genre")
    var books: MutableSet<Book> = mutableSetOf()

}