package coffee.store.payload.common

data class ScorePayload(
        val id: Long?,
        val itemId: Long,
        val score: Int,
        val comment: String?,
)
