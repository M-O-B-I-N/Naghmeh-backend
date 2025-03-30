package mobin.shabanifar.service

import mobin.shabanifar.models.ApiResponse
import mobin.shabanifar.models.common.PaginatedResponse
import mobin.shabanifar.models.verse.AdvancedVerseSearchResponse
import mobin.shabanifar.models.verse.RandomVerse
import mobin.shabanifar.models.verse.VerseOfPoem
import mobin.shabanifar.repository.VerseRepository

class VerseService(private val repository: VerseRepository) {

    fun advancedVerseSearch(
        verseText: String,
        poetName: String?,
        categoryName: String?,
        excludePoetName: String?,
        page: Int,
        pageSize: Int
    ): ApiResponse<PaginatedResponse<AdvancedVerseSearchResponse>> {
        return repository.advancedVerseSearch(
            verseText = verseText,
            poetName = poetName,
            categoryName = categoryName,
            excludePoetName = excludePoetName,
            page = page,
            pageSize = pageSize
        )
    }

    fun getRandomVerse(): ApiResponse<RandomVerse> {
        return repository.getRandomVerse()
    }

    fun getVersesOfPoem(poetName: String, categoryName: String, poemTitle: String): ApiResponse<List<VerseOfPoem>> {
        return repository.getVersesOfPoem(
            poetName = poetName,
            categoryName = categoryName,
            poemTitle = poemTitle
        )
    }

}