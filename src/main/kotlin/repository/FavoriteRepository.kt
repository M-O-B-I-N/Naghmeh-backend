package mobin.shabanifar.repository

import mobin.shabanifar.models.Poem
import mobin.shabanifar.models.favorite.Favorite
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class FavoriteRepository {
    fun isPoemExists(poemId: Int): Boolean {
        return Poem.select { Poem.id eq poemId }.count() > 0
    }

    fun saveFavoritePoem(poemId: Int) {
        Favorite.insert {
            it[Favorite.poemId] = poemId
        }
    }
}