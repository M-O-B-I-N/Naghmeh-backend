package mobin.shabanifar.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Poet : Table("poet") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 20)
    val catId = integer("cat_id").nullable()
    val description = text("description").nullable()

    override val primaryKey = PrimaryKey(id)
}

fun getPoetsByCentury(century: Int): List<PoetWithBirthYear> = transaction {
    return@transaction Poet.selectAll().mapNotNull {
        val description = it[Poet.description]
        val birthYear = extractBirthYearFromDescription(description)
        if (birthYear != null && (birthYear / 100 + 1) == century) {
            PoetWithBirthYear(
                id = it[Poet.id],
                name = it[Poet.name],
                catId = it[Poet.catId],
                description = description,
                birthYear = birthYear
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

fun extractBirthYearFromDescription(description: String?): Int? {
    if (description == null) return null

    // Regex to find the text after "متولد" or "زاده"
    val birthKeywordRegex = Regex("""(متولد|زاده)\s*(.*)""")
    val birthKeywordMatch = birthKeywordRegex.find(description)
    if (birthKeywordMatch != null) {
        val textAfterKeyword = birthKeywordMatch.groupValues[2]

        // Regex to match 3 or 4-digit numbers (e.g., 1325, 385)
        val numberRegex = Regex("""\b\d{3,4}\b""")
        val numberMatch = numberRegex.find(textAfterKeyword)
        if (numberMatch != null) {
            return numberMatch.value.toInt()
        }

        // Regex to match Persian ordinal words (e.g., چهارم, سوم, سیزده‌ام, etc.)
        val persianOrdinalRegex =
            Regex("""(یکم|دوم|سوم|چهارم|پنجم|ششم|هفتم|هشتم|نهم|دهم|یازدهم|دوازدهم|سیزدهم|چهاردهم|پانزدهم|شانزدهم|هفدهم|هجدهم|نوزدهم|بیستم|سی‌ام|چهل‌ام|پنجاه‌ام|شصت‌ام|هفتاد‌ام|هشتاد‌ام|نود‌ام|صد‌ام|یک|دو|سه|چهار|پنج|شش|هفت|هشت|نه|ده|یازده|دوازده|سیزده|چهارده|پانزده|شانزده|هفده|هجده|نوزده|بیست|سی|چهل|پنجاه|شصت|هفتاد|هشتاد|نود|صد)""")
        val persianMatch = persianOrdinalRegex.find(textAfterKeyword)
        if (persianMatch != null) {
            return when (persianMatch.value) {
                // Ordinal words
                "یکم", "یک" -> 100
                "دوم", "دو" -> 200
                "سوم", "سه" -> 300
                "چهارم", "چهار" -> 400
                "پنجم", "پنج" -> 500
                "ششم", "شش" -> 600
                "هفتم", "هفت" -> 700
                "هشتم", "هشت" -> 800
                "نهم", "نه" -> 900
                "دهم", "ده" -> 1000
                "یازدهم", "یازده" -> 1100
                "دوازدهم", "دوازده" -> 1200
                "سیزدهم", "سیزده" -> 1300
                "چهاردهم", "چهارده" -> 1400
                "پانزدهم", "پانزده" -> 1500
                "شانزدهم", "شانزده" -> 1600
                "هفدهم", "هفده" -> 1700
                "هجدهم", "هجده" -> 1800
                "نوزدهم", "نوزده" -> 1900
                "بیستم", "بیست" -> 2000
                "سی‌ام", "سی" -> 3000
                "چهل‌ام", "چهل" -> 4000
                "پنجاه‌ام", "پنجاه" -> 5000
                "شصت‌ام", "شصت" -> 6000
                "هفتاد‌ام", "هفتاد" -> 7000
                "هشتاد‌ام", "هشتاد" -> 8000
                "نود‌ام", "نود" -> 9000
                "صد‌ام", "صد" -> 10000
                else -> null
            }
        }
    }

    return null
}

data class PoetWithBirthYear(
    val id: Int,
    val name: String,
    val catId: Int?,
    val description: String?,
    val birthYear: Int?
)

data class Category(
    val text: String?,
    val url: String?
)
