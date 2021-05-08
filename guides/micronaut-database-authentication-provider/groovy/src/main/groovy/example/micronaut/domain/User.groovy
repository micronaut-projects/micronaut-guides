package example.micronaut.domain

import example.micronaut.UserState
import grails.gorm.annotation.Entity
import org.grails.datastore.gorm.GormEntity

@Entity // <1>
class User implements GormEntity<User>, UserState { // <2>
    String email
    String username
    String password
    boolean enabled = true
    boolean accountExpired = false
    boolean accountLocked = false
    boolean passwordExpired = false

    static constraints = {
        email nullable: false, blank: false
        username nullable: false, blank: false, unique: true
        password nullable: false, blank: false, password: true
    }

    static mapping = {
        password column: '`password`'
    }
}
