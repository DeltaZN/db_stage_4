package ru.itmo.coffee.store.model

import java.time.LocalDate

class Customer(var id: Long,
               var firstName: String,
               var lastName: String,
               var sex: Sex,
               var birthDay: LocalDate?,
               var address: Address?,
               var email: String?,
               var phone: String) {

    val favoriteCoffees: MutableList<Coffee> = ArrayList()
    val favoriteSchedules: MutableList<Schedule> = ArrayList()

}