package coffee.store.repository.mapper

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import coffee.store.dao.Address
import java.sql.ResultSet

@Component
class AddressMapper : RowMapper<Address> {
    override fun mapRow(rs: ResultSet, i: Int): Address =
         Address(
                rs.getLong("id"),
                rs.getString("страна"),
                rs.getString("субъект"),
                rs.getString("муниципальный_район"),
                rs.getString("поселение"),
                rs.getString("населенный_пункт"),
                rs.getString("планировочная_структура"),
                rs.getString("улица"),
                rs.getString("номер_земельного_участка"),
                rs.getString("номер_здания"),
                rs.getString("номер_помещения"),
        )
}