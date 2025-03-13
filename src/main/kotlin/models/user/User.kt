package mobin.shabanifar.models.user

import org.jetbrains.exposed.sql.Table

object User : Table("user") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50).nullable()
    val email = varchar("email", 50).uniqueIndex()
    val password = varchar("password", 60)

    override val primaryKey = PrimaryKey(id)
}