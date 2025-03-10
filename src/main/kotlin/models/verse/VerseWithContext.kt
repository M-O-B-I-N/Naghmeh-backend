package mobin.shabanifar.models.verse

data class VerseWithContext(
    val previousVerse: String?,
    val matchedVerse: String?,
    val nextVerse: String?
)
