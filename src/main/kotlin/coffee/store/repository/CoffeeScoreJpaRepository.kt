package coffee.store.repository

import coffee.store.entity.CoffeeScore
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface CoffeeScoreJpaRepository : CrudRepository<CoffeeScore, Long> {
    @Query(
            value = "select * from оценка_кофе join оценка on оценка_кофе.id_оценки = оценка.id where id_кофе = ?",
            countQuery = "select count(*) from оценка_кофе where id_кофе = ?",
            nativeQuery = true)
    fun findScoresByCoffeeId(coffeeId: Long): Iterable<CoffeeScore>

    @Query(
            value = "select * from оценка_кофе join оценка on оценка_кофе.id_оценки = оценка.id where id_кофе = ? and id_клиента = ?",
            countQuery = "select count(*) from оценка_кофе join оценка on оценка_кофе.id_оценки = оценка.id where id_кофе = ? and id_клиента = ?",
            nativeQuery = true)
    fun findScoreByCoffeeIdAndUserId(coffeeId: Long, clientId: Long): Optional<CoffeeScore>
}