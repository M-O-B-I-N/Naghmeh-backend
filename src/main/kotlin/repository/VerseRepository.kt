package mobin.shabanifar.repository

import io.ktor.http.HttpStatusCode
import mobin.shabanifar.models.ApiResponse
import mobin.shabanifar.models.Cat
import mobin.shabanifar.models.common.PaginatedResponse
import mobin.shabanifar.models.poem.Poem
import mobin.shabanifar.models.poet.Poet
import mobin.shabanifar.models.verse.AdvancedVerseSearchRequest
import mobin.shabanifar.models.verse.AdvancedVerseSearchResponse
import mobin.shabanifar.models.verse.RandomVerse
import mobin.shabanifar.models.verse.Verse
import mobin.shabanifar.models.verse.VerseOfPoem
import mobin.shabanifar.models.verse.VerseWithContext
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.Random
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.Collections

class VerseRepository {
    fun getVersesOfPoem(poetName: String, categoryName: String, poemTitle: String): ApiResponse<List<VerseOfPoem>> =
        transaction {
            // Step 1: Find the poem_id of the specified poem
            val poemId = (Poem innerJoin Cat innerJoin Poet)
                .slice(Poem.id)
                .select {
                    (Poet.name eq poetName) and
                        (Cat.text eq categoryName) and
                        (Poem.title eq poemTitle)
                }
                .singleOrNull()?.get(Poem.id)
                ?: return@transaction ApiResponse.Success(Collections.emptyList())

            // Step 2: Fetch the verses of the specified poem
            val response = Verse
                .select { Verse.poemId eq poemId }
                .orderBy(Verse.vorder)
                .map {
                    VerseOfPoem(
                        poemId = it[Verse.poemId],
                        vorder = it[Verse.vorder],
                        position = it[Verse.position],
                        text = it[Verse.text]
                    )
                }
            return@transaction ApiResponse.Success(response)
        }

    fun getRandomVerse(): ApiResponse<RandomVerse> = transaction {
        // Step 1: Select a random verse with position 0
        val firstVerse = Verse
            .slice(Verse.text, Verse.poemId, Verse.vorder)
            .select { Verse.position eq 0 }
            .orderBy(Random())
            .limit(1)
            .map {
                Triple(
                    it[Verse.text] ?: "",
                    it[Verse.poemId],
                    it[Verse.vorder]
                )
            }.firstOrNull() ?: return@transaction ApiResponse.Error(
            status = HttpStatusCode.NotFound,
            message = "No verse found in the database."
        )

        val (firstVerseText, poemId, firstVerseVorder) = firstVerse

        // Step 2: Select the next verse with position 1 from the same poem
        val secondVerseText = Verse
            .slice(Verse.text)
            .select {
                (Verse.poemId eq poemId) and
                    (Verse.vorder eq firstVerseVorder + 1) // Next Vorder
            }
            .map { it[Verse.text] ?: "" }
            .firstOrNull() ?: return@transaction ApiResponse.Error(
            status = HttpStatusCode.NotFound,
            message = "No matching verse found for poem ID $poemId."
        )

        // Step 3: Fetch the poem and poet details
        val poemWithPoet = (Poem innerJoin Cat innerJoin Poet)
            .slice(Poem.id, Poem.title, Poet.name, Poet.id)
            .select { Poem.id eq poemId }
            .map {
                Triple(
                    it[Poem.title],
                    it[Poet.name],
                    it[Poet.id]
                )
            }.firstOrNull() ?: return@transaction ApiResponse.Error(
            status = HttpStatusCode.NotFound,
            message = "No poem or poet found for poem ID $poemId."
        )

        val (poemTitle, poetName, poetId) = poemWithPoet

        // Step 4: Return the result in the RandomVerse data class
        val response = RandomVerse(
            verses = listOf(firstVerseText, secondVerseText),
            poemId = poemId,
            poemTitle = poemTitle,
            poetName = poetName,
            poetId = poetId
        )
        return@transaction ApiResponse.Success(response)
    }

    fun advancedVerseSearch(
        request: AdvancedVerseSearchRequest
    ): ApiResponse<PaginatedResponse<AdvancedVerseSearchResponse>> = transaction {
        // Step 1: Find verses that match the given text and optional filters
        val verseQuery = Verse
            .innerJoin(Poem).innerJoin(Cat).innerJoin(Poet)
            .slice(
                Verse.poemId,
                Verse.vorder,
                Verse.text,
                Poet.name,
                Cat.text,
                Poem.title
            )
            .select {
                Verse.text like "%${request.verseText}%" and
                    (request.poetName?.let { Poet.name eq it } ?: Op.TRUE) and // Filter by poet name
                    (request.categoryName?.let { Cat.text eq it } ?: Op.TRUE) and // Filter by category name
                    (request.excludePoetName?.let { Poet.name neq it } ?: Op.TRUE) // Exclude poet if provided
            }
            .orderBy(Verse.poemId to SortOrder.ASC, Verse.vorder to SortOrder.ASC)

        // Step 2: Count the total number of matching verses
        val totalCount: Long = verseQuery.count()

        // Step 3: Apply pagination
        val paginatedVerses = verseQuery
            .limit(request.pageSize, offset = ((request.page - 1) * request.pageSize).toLong())
            .toList()

        // If no verses match, return an empty response
        if (paginatedVerses.isEmpty()) {
            return@transaction ApiResponse.Success(PaginatedResponse(Collections.emptyList(), totalCount))
        }

        // Step 4: Fetch the previous and next verses for each matched verse
        val results = paginatedVerses.map { verseRow ->
            val poemId = verseRow[Verse.poemId]
            val vorder = verseRow[Verse.vorder]

            // Fetch the previous verse
            val previousVerse = Verse
                .slice(Verse.text)
                .select {
                    (Verse.poemId eq poemId) and (Verse.vorder eq (vorder - 1))
                }
                .singleOrNull()?.get(Verse.text)

            // Fetch the next verse
            val nextVerse = Verse
                .slice(Verse.text)
                .select {
                    (Verse.poemId eq poemId) and (Verse.vorder eq (vorder + 1))
                }
                .singleOrNull()?.get(Verse.text)

            // Step 5: Construct the response
            AdvancedVerseSearchResponse(
                poetName = verseRow[Poet.name],
                categoryName = verseRow[Cat.text],
                poemTitle = verseRow[Poem.title],
                matchedVerse = VerseWithContext(
                    previousVerse = previousVerse,
                    matchedVerse = verseRow[Verse.text] ?: "",
                    nextVerse = nextVerse
                )
            )
        }

        // Step 6: Return the paginated response
        return@transaction ApiResponse.Success(PaginatedResponse(results, totalCount))
    }
}
