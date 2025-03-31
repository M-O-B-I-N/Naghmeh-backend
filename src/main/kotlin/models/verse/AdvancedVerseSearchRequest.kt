package mobin.shabanifar.models.verse

data class AdvancedVerseSearchRequest(
    val verseText: String,
    val poetName: String? = null, // Optional: Filter by poet name
    val categoryName: String? = null, // Optional: Filter by category name
    val excludePoetName: String? = null, // Optional: Exclude a specific poet
    val page: Int = 1,
    val pageSize: Int = 10
)
