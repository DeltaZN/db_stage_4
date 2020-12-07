package coffee.store.payload.request.constructor

import coffee.store.model.CoffeeStatus

data class CoffeeConstructRequest(
        val id: Long?,
        val name: String?,
        val status: CoffeeStatus?,
        val photo: ByteArray?,
        val components: List<CoffeeConstructComponent>?,
)
