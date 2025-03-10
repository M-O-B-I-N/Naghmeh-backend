package mobin.shabanifar.models.common

data class PaginatedResponse<T>(
    val results: List<T>,
    val totalCount: Long
)
