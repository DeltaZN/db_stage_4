package ru.itmo.coffee.store.model

abstract class Product(var id: Long,
                       var name: String,
                       var cost: Double,
                       var photo: ByteArray?)