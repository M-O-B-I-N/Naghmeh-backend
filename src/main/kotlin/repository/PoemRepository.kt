package mobin.shabanifar.repository

import mobin.shabanifar.models.ApiResponse
import mobin.shabanifar.models.Cat
import mobin.shabanifar.models.common.PaginatedResponse
import mobin.shabanifar.models.poem.Poem
import mobin.shabanifar.models.poem.PoemsOfCategoryResponse
import mobin.shabanifar.models.poet.Poet
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class PoemRepository {
    fun getPoemsOfCategory(
        poetName: String,
        categoryName: String,
        page: Int,
        pageSize: Int
    ): ApiResponse<PaginatedResponse<PoemsOfCategoryResponse>> = transaction {
        val poemQuery = Poem.innerJoin(Cat).innerJoin(Poet)
            .slice(Poem.id, Poem.title, Poem.url)
            .select {
                (Poet.name eq poetName) and (Cat.text eq categoryName)
            }
        // Step 2: Count the total number of matching verses
        val totalCount: Long = poemQuery.count()

        // Step 3: Apply pagination
        val paginatedPoems = poemQuery
            .limit(pageSize, offset = ((page - 1) * pageSize).toLong())
            .toList()

        // If no verses match, return an empty response
        if (paginatedPoems.isEmpty()) {
            return@transaction ApiResponse.Success(PaginatedResponse(Collections.emptyList(), totalCount))
        }

        val results = paginatedPoems.map {
            PoemsOfCategoryResponse(
                id = it[Poem.id],
                title = it[Poem.title],
                url = it[Poem.url]
            )
        }

        return@transaction ApiResponse.Success(PaginatedResponse(results, totalCount))
    }

}