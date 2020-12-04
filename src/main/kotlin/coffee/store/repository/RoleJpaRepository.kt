package coffee.store.repository

import coffee.store.auth.ERole
import coffee.store.entity.Role
import org.springframework.data.repository.CrudRepository
import java.util.*

interface RoleJpaRepository : CrudRepository<Role, Long> {
    fun findRoleByName(name: ERole): Optional<Role>
}