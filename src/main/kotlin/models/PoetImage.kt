package mobin.shabanifar.models

import org.jetbrains.exposed.sql.Table

object PoetImage : Table("poet_image") {
    val id = integer("id").autoIncrement()
    val poetId = integer("poet_id").references(Poet.id)
    val url = text("url")

    override val primaryKey = PrimaryKey(id)
}

data class PoetImageResponse(
    val id: Int,
    val poetId: Int,
    val url: String
)

