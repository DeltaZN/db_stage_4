package ru.itmo.coffee.store.dao

class Schedule(val id: Long, val name: String?, val author: Customer, val description: String?,
               val status: String)