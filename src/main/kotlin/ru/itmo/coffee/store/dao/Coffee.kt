package ru.itmo.coffee.store.dao

class Coffee(id: Long, name: String, cost: Double, photo: ByteArray, val type: Char, val state: String, val author: Customer?)
    : Product(id, name, cost, photo)