package mobin.shabanifar.models

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.Collections.emptyList

object Verse : Table("verse") {
    val poemId = integer("poem_id").references(Poem.id)
    val vorder = integer("vorder")
    val position = integer("position")
    val text = text("text").nullable()
}

fun advancedVerseSearch(
    verseText: String,
    poetName: String? = null, // Optional: Filter by poet name
    categoryName: String? = null, // Optional: Filter by category name
    excludePoetName: String? = null, // Optional: Exclude a specific poet
    page: Int = 1, // Default to page 1
    pageSize: Int = 10 // Default to 10 items per page
): PaginatedResponse = transaction {
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
            Verse.text like "%$verseText%" and
                    (poetName?.let { Poet.name eq it } ?: Op.TRUE) and // Filter by poet name
                    (categoryName?.let { Cat.text eq it } ?: Op.TRUE) and // Filter by category name
                    (excludePoetName?.let { Poet.name neq it } ?: Op.TRUE) // Exclude poet if provided
        }
        .orderBy(Verse.poemId to SortOrder.ASC, Verse.vorder to SortOrder.ASC)

    // Step 2: Count the total number of matching verses
    val totalCount : Long = verseQuery.count()

    // Step 3: Apply pagination
    val paginatedVerses = verseQuery
        .limit(pageSize, offset = ((page - 1) * pageSize).toLong())
        .toList()

    // If no verses match, return an empty response
    if (paginatedVerses.isEmpty()) {
        return@transaction PaginatedResponse(emptyList(), totalCount)
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
                matchedVerse = verseRow[Verse.text]?: "",
                nextVerse = nextVerse
            )
        )
    }

    // Step 6: Return the paginated response
    return@transaction PaginatedResponse(results, totalCount)
}

data class AdvancedVerseSearchResponse(
    val poetName: String?, // Allow null
    val categoryName: String?, // Allow null
    val poemTitle: String?, // Allow null
    val matchedVerse: VerseWithContext
)

data class VerseWithContext(
    val previousVerse: String?,
    val matchedVerse: String?, // Allow null
    val nextVerse: String?
)
data class PaginatedResponse(
    val results: List<AdvancedVerseSearchResponse>,
    val totalCount: Long
)
