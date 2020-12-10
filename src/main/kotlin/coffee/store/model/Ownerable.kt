package coffee.store.model

import coffee.store.entity.User

interface Ownerable {
    val owner: User
}