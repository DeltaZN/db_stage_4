package ru.itmo.coffee.store.model

import java.time.LocalDate

class Customer(val id: Long, val firstName: String, val lastName: String, val sex: Char,
            val birthDay: LocalDate?, val address: Address?, val email: String?, val phone: String?)