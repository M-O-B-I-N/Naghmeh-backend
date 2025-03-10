package mobin.shabanifar.models.verse

import mobin.shabanifar.models.Poem
import org.jetbrains.exposed.sql.Table

object Verse : Table("verse") {
    val poemId = integer("poem_id").references(Poem.id)
    val vorder = integer("vorder")
    val position = integer("position")
    val text = text("text").nullable()
}
