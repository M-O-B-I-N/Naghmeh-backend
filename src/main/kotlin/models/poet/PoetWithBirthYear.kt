package mobin.shabanifar.models.poet

data class PoetWithBirthYear(
    val id: Int,
    val name: String,
    val catId: Int?,
    val description: String?,
    val birthYear: Int?
)
