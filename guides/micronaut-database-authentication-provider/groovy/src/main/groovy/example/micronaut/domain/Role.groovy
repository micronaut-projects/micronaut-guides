package example.micronaut.domain

import grails.gorm.annotation.Entity
import org.grails.datastore.gorm.GormEntity
import io.micronaut.serde.annotation.Serdeable

@Serdeable
@Entity // <1>
class Role implements GormEntity<Role> {  // <2>
    String authority

    static constraints = {
        authority nullable: false, unique: true
    }
}
