package mobin.shabanifar.models.verse

data class RandomVerse(
    val verses: List<String>,
    val poemId: Int,
    val poemTitle: String,
    val poetName: String,
    val poetId: Int
)
