package ru.itmo.coffee.store.model

class Dessert(id: Long, val calories: Double, val weight: Double?, name: String, cost: Double, photo: ByteArray?) : Product(id, name, cost, photo)