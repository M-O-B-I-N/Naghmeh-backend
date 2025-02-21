package mobin.shabanifar.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object Poem : Table("poem") {
    val id = integer("id").autoIncrement()
    val catId = integer("cat_id").references(Cat.id)
    val title = varchar("title", 255)
    val url = varchar("url", 255)

    override val primaryKey = PrimaryKey(id)
}

fun getPoemsOfCategory(poetName: String, categoryName: String): List<Poems> = transaction {
    return@transaction (Poem innerJoin Cat innerJoin Poet)
        .slice(Poem.id, Poem.title, Poem.url)
        .select {
            (Poet.name eq poetName) and (Cat.text eq categoryName)
        }.map {
            Poems(
                id = it[Poem.id],
                title = it[Poem.title],
                url = it[Poem.url]
            )
        }
}

data class Poems(
    val id: Int,
    val title: String?,
    val url: String?
)
