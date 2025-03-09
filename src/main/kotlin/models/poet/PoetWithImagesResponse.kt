package mobin.shabanifar.models.poet

data class PoetWithImagesResponse(
    val poetId: Int,
    val poetName: String,
    val description: String?,
    val images: List<String>
)
