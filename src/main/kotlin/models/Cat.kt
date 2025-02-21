package mobin.shabanifar.models

import org.jetbrains.exposed.sql.*

object Cat : Table("cat") {
    val id = integer("id").autoIncrement()
    val poetId = integer("poet_id").references(Poet.id).nullable()
    val text = varchar("text", 100).nullable()
    val parentId = integer("parent_id").nullable()
    val url = varchar("url", 255).nullable()

    override val primaryKey = PrimaryKey(id) // Define primary key
}
