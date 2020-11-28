package ru.itmo.coffee.store.dao

abstract class Product(val id: Long, val name: String, val cost: Double, val photo: ByteArray?)