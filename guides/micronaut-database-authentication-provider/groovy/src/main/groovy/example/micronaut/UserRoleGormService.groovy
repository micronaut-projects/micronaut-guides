package example.micronaut

import example.micronaut.domain.Role
import example.micronaut.domain.User
import example.micronaut.domain.UserRole
import grails.gorm.services.Query
import grails.gorm.services.Service

@Service(UserRole) // <1>
interface UserRoleGormService {

    UserRole save(User user, Role role)

    UserRole find(User user, Role role)

    void delete(Serializable id)

    @Query("""select $r.authority
    from ${UserRole ur}
    inner join ${User u = ur.user}
    inner join ${Role r = ur.role}
    where $u.username = $username""") // <2>
    List<String> findAllAuthoritiesByUsername(String username)
}
