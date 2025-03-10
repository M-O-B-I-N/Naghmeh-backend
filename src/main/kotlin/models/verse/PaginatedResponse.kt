package mobin.shabanifar.models.verse

data class PaginatedResponse(
    val results: List<AdvancedVerseSearchResponse>,
    val totalCount: Long
)
