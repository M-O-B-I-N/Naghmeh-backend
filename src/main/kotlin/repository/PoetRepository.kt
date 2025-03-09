package mobin.shabanifar.repository

import mobin.shabanifar.models.Cat
import mobin.shabanifar.models.poet.*
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

    fun getPoetImages(poetId: Int): PoetImageResponse {
        val urls = transaction {
            PoetImage.select { PoetImage.poetId eq poetId }.map {
                it[PoetImage.url]
            }
        }
        return PoetImageResponse(url = urls)
    }

    fun getPoetWithImages(poetId: Int): PoetWithImagesResponse? {
        return transaction {
            // Fetch the poem
            val poet = Poet.select { Poet.id eq poetId }.singleOrNull() ?: return@transaction null

            // Fetch the poet's images
            val images = PoetImage.select { PoetImage.poetId eq poet[Poet.id] }.map { it[PoetImage.url] }

            // Fetch the poet's name
            val poetName = Poet.select { Poet.id eq poetId }.singleOrNull()?.get(Poet.name)
                ?: "Unknown Poet"

            // Construct the response
            PoetWithImagesResponse(
                poetId = poet[Poet.id],
                poetName = poetName,
                description = poet[Poet.description],
                images = images
            )
        }
    }

}
