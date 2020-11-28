package ru.itmo.coffee.store.model

enum class Sex {
    F {
        override fun toString() = "Ж"
    },
    M {
        override fun toString() = "М"
    }
}