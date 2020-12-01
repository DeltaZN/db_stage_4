package coffee.store.dao

class Coffee(
        id: Long,
        name: String,
        cost: Double,
        photo: ByteArray,
        val type: Char,
        val state: String,
        val author: User?,
        val components: MutableList<CoffeeComponent>,
) : Product(id, name, cost, photo)