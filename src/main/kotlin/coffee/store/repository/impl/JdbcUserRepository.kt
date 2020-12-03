package coffee.store.repository.impl

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import coffee.store.dao.Address
import coffee.store.dao.User
import coffee.store.repository.jpa.AddressJpaRepository
import coffee.store.repository.UserRepository
import coffee.store.repository.mapper.UserMapper
import org.springframework.dao.EmptyResultDataAccessException
import java.util.*
import javax.annotation.PostConstruct
import javax.sql.DataSource
import kotlin.collections.HashMap


@Repository
class JdbcUserRepository(
        private val jdbcTemplate: JdbcTemplate,
        private val rowMapper: UserMapper,
        private val addressRepository: AddressJpaRepository,
        private val dataSource: DataSource
) : UserRepository {

    private lateinit var jdbcInsert: SimpleJdbcInsert
    private val cache: HashMap<Long, Address> = HashMap()

    @PostConstruct
    fun init() {
        jdbcInsert = SimpleJdbcInsert(dataSource)
                .withTableName("клиент").usingGeneratedKeyColumns("id");
    }

    override fun save(user: User): Long {
        user.address?.let { addressRepository.save(it) }
        val parameters = HashMap<String, Any?>()
        parameters["имя"] = user.firstName
        parameters["фамилия"] = user.lastName
        parameters["пол"] = user.sex
        parameters["дата_рождения"] = user.birthDay
        parameters["id_адреса"] = user.address?.id
        parameters["email"] = user.email
        parameters["телефон"] = user.phone
        parameters["пароль"] = user.password
        parameters["роль"] = user.role.toString()
        user.id = jdbcInsert.executeAndReturnKey(parameters).toLong()
        return user.id
    }

    override fun update(user: User): Int {
        user.address?.let { addressRepository.save(it) }
        return jdbcTemplate.update(
                "update клиент set имя = ?, фамилия = ?, пол = ?, дата_рождения = ?, id_адреса = ?, email = ?, телефон = ?, пароль = ?, роль = ? where id = ?",
                user.firstName, user.lastName, user.sex, user.birthDay,
                user.address?.id, user.email, user.phone, user.password, user.role, user.id)
    }

    override fun deleteById(id: Long): Int {
        return jdbcTemplate.update(
                "delete from клиент where id = ?", id)
    }

    override fun findAll(): List<User> {
        return jdbcTemplate.query("select * from клиент", rowMapper)
    }

    override fun findById(id: Long): Optional<User> = Optional.ofNullable(
            jdbcTemplate.queryForObject(
            "select * from клиент where id = ?", rowMapper, id))

    override fun findByPhone(phone: String): Optional<User> {
        return try {
            Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            "select * from клиент where телефон = ?", rowMapper, phone))
        } catch (e: EmptyResultDataAccessException) {
            Optional.empty()
        }
    }
}