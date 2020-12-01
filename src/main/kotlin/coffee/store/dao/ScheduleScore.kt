package coffee.store.dao

class ScheduleScore(id: Long, score: Int, comment: String?, val schedule: Schedule) :
        Score(id, score, comment)