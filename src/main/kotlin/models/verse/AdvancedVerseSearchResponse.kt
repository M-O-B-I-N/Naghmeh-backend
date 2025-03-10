package mobin.shabanifar.models.verse

data class AdvancedVerseSearchResponse(
    val poetName: String?,
    val categoryName: String?,
    val poemTitle: String?,
    val matchedVerse: VerseWithContext
)