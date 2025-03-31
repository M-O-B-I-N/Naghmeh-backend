package mobin.shabanifar.service

import mobin.shabanifar.models.ApiResponse
import mobin.shabanifar.models.common.PaginatedResponse
import mobin.shabanifar.models.verse.AdvancedVerseSearchRequest
import mobin.shabanifar.models.verse.AdvancedVerseSearchResponse
import mobin.shabanifar.models.verse.RandomVerse
import mobin.shabanifar.models.verse.VerseOfPoem
import mobin.shabanifar.repository.VerseRepository

class VerseService(private val repository: VerseRepository) {

    fun advancedVerseSearch(
        request: AdvancedVerseSearchRequest
    ): ApiResponse<PaginatedResponse<AdvancedVerseSearchResponse>> {
        return repository.advancedVerseSearch(request)
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
