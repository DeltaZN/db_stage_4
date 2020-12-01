package coffee.store.entity

import coffee.store.dao.User

class Schedule(var id: Long,
               var name: String?,
               var author: User,
               var description: String?,
               var status: String)