package example.micronaut.domain

import javax.persistence.*

@Entity
@Table(name = "book")
class Book(@Id
           @GeneratedValue(strategy = GenerationType.AUTO)
           var id: Long,

           @Column(name = "name", nullable = false)
           var name: String,

           @Column(name = "isbn", nullable = false)
           var isbn: String
) {


    @ManyToOne
    var genre: Genre? = null

}