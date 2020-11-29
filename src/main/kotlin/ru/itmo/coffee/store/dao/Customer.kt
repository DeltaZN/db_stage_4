package ru.itmo.coffee.store.dao

import ru.itmo.coffee.store.model.Sex
import java.time.LocalDate

data class Customer(var id: Long, val firstName: String, val lastName: String, val sex: Sex,
               val birthDay: LocalDate?, val address: Address?, val email: String?, val phone: String)