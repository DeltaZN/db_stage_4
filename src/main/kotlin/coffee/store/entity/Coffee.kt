package coffee.store.entity

import coffee.store.dao.User

class Coffee(
        id: Long,
        name: String,
        cost: Double,
        photo: ByteArray?,
        var type: Char,
        var state: String,
        var author: User,
) : Product(id, name, cost, photo)