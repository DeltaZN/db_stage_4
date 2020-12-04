package coffee.store.entity

class CoffeeScore(id: Long, score: Int, comment: String?, val coffee: Coffee) :
        Score(id, score, comment)