package ru.itmo.coffee.store.dao

class CoffeeScore(id: Long, score: Int, comment: String?, val coffee: Coffee) :
        Score(id, score, comment)