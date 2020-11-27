package ru.itmo.coffee.store.model

class ScheduleScore(id: Long, score: Int, comment: String?, val schedule: Schedule) :
        Score(id, score, comment)