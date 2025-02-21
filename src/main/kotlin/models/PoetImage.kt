package mobin.shabanifar.models

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object PoetImage : Table("poet_image") {
    val id = integer("id").autoIncrement()
    val poetId = integer("poet_id").references(Poet.id)
    val url = text("url")

    override val primaryKey = PrimaryKey(id)
}


suspend fun getPoetWithImages(poetId: Int): PoetWithImagesResponse? {
    return withContext(Dispatchers.IO) {
         transaction {
            // Fetch the poem
            val poet = Poet.select { Poet.id eq poetId }.singleOrNull() ?: return@transaction null

            // Fetch the poet's images
            val images = PoetImage.select { PoetImage.poetId eq poet[Poet.id] }.map { it[PoetImage.url] }

            // Fetch the poet's name
            val poetName = Poet.select { Poet.id eq poetId }.singleOrNull()?.get(Poet.name)
                ?: "Unknown Poet"

            // Construct the response
            PoetWithImagesResponse(
                poetId = poet[Poet.id],
                poetName = poetName,
                description = poet[Poet.description],
                images = images
            )
        }
    }
}

data class PoetWithImagesResponse(
    val poetId: Int,
    val poetName: String,
    val description: String?,
    val images: List<String>
)

data class PoetImageResponse(
    val id: Int,
    val poetId: Int,
    val url: String
)
