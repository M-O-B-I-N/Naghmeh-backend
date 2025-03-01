package mobin.shabanifar.models.favorite

import org.jetbrains.exposed.sql.Table

object Favorite : Table("fav") {
    val id = integer("id").autoIncrement()
    val poemId = integer("poem_id")

    override val primaryKey = PrimaryKey(id)
}
