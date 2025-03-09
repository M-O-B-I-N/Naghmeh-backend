package mobin.shabanifar.models.poet

import org.jetbrains.exposed.sql.Table

object PoetImage : Table("poet_image") {
    val id = integer("id").autoIncrement()
    val poetId = integer("poet_id").references(Poet.id)
    val url = text("url")

    override val primaryKey = PrimaryKey(id)
}
