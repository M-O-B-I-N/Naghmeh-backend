package mobin.shabanifar.repository

import mobin.shabanifar.models.Cat
import mobin.shabanifar.models.poet.Category
import mobin.shabanifar.models.poet.FamousPoet
import mobin.shabanifar.models.poet.Poet
import mobin.shabanifar.models.poet.PoetWithBirthYear
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class PoetRepository {

    fun getPoetsByCentury(century: Int): List<PoetWithBirthYear?> = transaction {
        return@transaction Poet.selectAll().mapNotNull {
            if (it[Poet.birthYearInLHijri] != null && ((it[Poet.birthYearInLHijri]?.div(100) ?: 0) + 1) == century) {
                PoetWithBirthYear(
                    id = it[Poet.id],
                    name = it[Poet.name],
                    catId = it[Poet.catId],
                    description = it[Poet.description],
                    birthYear = it[Poet.birthYearInLHijri]
                )
            } else {
                null
            }
        }
    }

    fun getWorksOfPoet(poetName: String): List<Category> = transaction {
        return@transaction (Cat innerJoin Poet)
            .slice(Cat.text, Cat.url)
            .select {
                (Poet.name eq poetName) and (Cat.parentId neq 0)
            }.map {
                Category(
                    text = it[Cat.text],
                    url = it[Cat.url]
                )
            }
    }

    fun getTop8FamousPoets(): List<FamousPoet> = transaction {
        return@transaction Poet
            .slice(Poet.id, Poet.name, Poet.description) // Select the fields you need
            .selectAll()
            .limit(8) // Limit the result to 8 poets
            .map {
                FamousPoet(
                    id = it[Poet.id],
                    name = it[Poet.name],
                    description = it[Poet.description]
                )
            }
    }

}
