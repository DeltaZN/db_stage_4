package coffee.store.repository.mapper

import coffee.store.auth.ERole
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import coffee.store.dao.User
import coffee.store.entity.Sex
import coffee.store.repository.AddressRepository
import java.sql.ResultSet

@Component
class UserMapper(private val addressRepository: AddressRepository) : RowMapper<User> {
    override fun mapRow(rs: ResultSet, rowNum: Int): User =
            User(
                    rs.getLong("id"),
                    rs.getString("имя"),
                    rs.getString("фамилия"),
                    Sex.valueOf(rs.getString("пол")),
                    rs.getDate("дата_рождения")?.toLocalDate(),
                    addressRepository.findById(rs.getLong("id_адреса")).orElse(null),
                    rs.getString("email"),
                    rs.getString("телефон"),
                    rs.getString("пароль"),
                    ERole.valueOf(rs.getString("роль")),
            )
}