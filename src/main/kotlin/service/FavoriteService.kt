package mobin.shabanifar.service

import io.ktor.http.*
import mobin.shabanifar.repository.FavoriteRepository
import mobin.shabanifar.utils.CustomException

class FavoriteService(private val favoriteRepository: FavoriteRepository) {
    fun saveFavoritePoem(poemId: Int) {
        // Check if the poem exists
        if (!favoriteRepository.isPoemExists(poemId)) {
            throw CustomException(HttpStatusCode.NotFound, "Poem with id $poemId does not exist in the database.")
        }

        // Save the favorite poem
        favoriteRepository.saveFavoritePoem(poemId)
    }
}