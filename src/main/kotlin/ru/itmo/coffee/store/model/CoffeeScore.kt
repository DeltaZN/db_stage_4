package ru.itmo.coffee.store.model

class CoffeeScore(id: Long, score: Int, comment: String?, val coffee: Coffee) :
        Score(id, score, comment)