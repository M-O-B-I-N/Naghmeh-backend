package mobin.shabanifar.models.poem

import mobin.shabanifar.models.Cat
import org.jetbrains.exposed.sql.Table

object Poem : Table("poem") {
    val id = integer("id").autoIncrement()
    val catId = integer("cat_id").references(Cat.id)
    val title = varchar("title", 255)
    val url = varchar("url", 255)

    override val primaryKey = PrimaryKey(id)
}
