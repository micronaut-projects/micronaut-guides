package example.micronaut

import example.micronaut.domain.Role
import grails.gorm.services.Service

@Service(Role) // <1>
interface RoleGormService {
    Role save(String authority)

    Role find(String authority)

    void delete(Serializable id)
}
