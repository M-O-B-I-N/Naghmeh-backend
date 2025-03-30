package mobin.shabanifar.repository

import io.ktor.http.*
import mobin.shabanifar.models.ApiResponse
import mobin.shabanifar.models.Cat
import mobin.shabanifar.models.poet.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class PoetRepository {

    fun getPoetsByCentury(century: Int): ApiResponse<List<PoetWithBirthYear?>> = transaction {
        val response = Poet.selectAll().mapNotNull {
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
        return@transaction ApiResponse.Success(response)
    }

    fun getWorksOfPoet(poetName: String): ApiResponse<List<Category>> = transaction {
        val response = (Cat innerJoin Poet)
            .slice(Cat.text, Cat.url)
            .select {
                (Poet.name eq poetName) and (Cat.parentId neq 0)
            }.map {
                Category(
                    text = it[Cat.text],
                    url = it[Cat.url]
                )
            }
        return@transaction ApiResponse.Success(response)
    }

    fun getTop8FamousPoets(): ApiResponse<List<FamousPoet>> = transaction {
        val response = Poet
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
        return@transaction ApiResponse.Success(response)
    }

    fun getPoetImages(poetId: Int): ApiResponse<PoetImageResponse> {
        val urls = transaction {
            PoetImage.select { PoetImage.poetId eq poetId }.map {
                it[PoetImage.url]
            }
        }
        return ApiResponse.Success(PoetImageResponse(url = urls))
    }

    fun getPoetWithImages(poetId: Int): ApiResponse<PoetWithImagesResponse> {
        return transaction {
            // Fetch the poem
            val poet = Poet.select { Poet.id eq poetId }.singleOrNull()
                ?: return@transaction ApiResponse.Error(status = HttpStatusCode.NotFound, message = "Poet not found")

            // Fetch the poet's images
            val images = PoetImage.select { PoetImage.poetId eq poet[Poet.id] }.map { it[PoetImage.url] }

            // Fetch the poet's name
            val poetName = Poet.select { Poet.id eq poetId }.singleOrNull()?.get(Poet.name)
                ?: "Unknown Poet"

            // Construct the response
            ApiResponse.Success(
                PoetWithImagesResponse(
                    poetId = poet[Poet.id],
                    poetName = poetName,
                    description = poet[Poet.description],
                    images = images
                )
            )
        }
    }

}
