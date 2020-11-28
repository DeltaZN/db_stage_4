package ru.itmo.coffee.store.model

import ru.itmo.coffee.store.dao.Customer

class Schedule(var id: Long,
               var name: String?,
               var author: Customer,
               var description: String?,
               var status: String)