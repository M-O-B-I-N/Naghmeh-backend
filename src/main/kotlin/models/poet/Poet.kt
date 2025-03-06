package mobin.shabanifar.models.poet

import org.jetbrains.exposed.sql.Table

object Poet : Table("poet") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 20)
    val catId = integer("cat_id").nullable()
    val description = text("description").nullable()
    val birthYearInLHijri = integer("birthYearInLHijri").nullable()
    val deathYearInLHijri = integer("deathYearInLHijri").nullable()
    val birthPlace = text("birthPlace").nullable()
    val deathPlace = text("deathPlace").nullable()

    override val primaryKey = PrimaryKey(id)
}
