package ru.itmo.coffee.store.model

import ru.itmo.coffee.store.dao.Customer

class Coffee(id: Long,
             name: String,
             cost: Double,
             photo: ByteArray?,
             var type: Char,
             var state: String,
             var author: Customer)
    : Product(id, name, cost, photo) {
}