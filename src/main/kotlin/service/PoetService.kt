package mobin.shabanifar.service

import mobin.shabanifar.models.ApiResponse
import mobin.shabanifar.models.poet.Category
import mobin.shabanifar.models.poet.FamousPoet
import mobin.shabanifar.models.poet.PoetImageResponse
import mobin.shabanifar.models.poet.PoetWithBirthYear
import mobin.shabanifar.models.poet.PoetWithImagesResponse
import mobin.shabanifar.repository.PoetRepository

class PoetService(private val poetRepository: PoetRepository) {

    fun getPoetsByCentury(century: Int): ApiResponse<List<PoetWithBirthYear?>> {
        return poetRepository.getPoetsByCentury(century)
    }

    fun getWorksOfPoet(poetName: String): ApiResponse<List<Category>> {
        return poetRepository.getWorksOfPoet(poetName)
    }

    fun getTop8FamousPoets(): ApiResponse<List<FamousPoet>> {
        return poetRepository.getTop8FamousPoets()
    }

    fun getPoetWithImages(poetId: Int): ApiResponse<PoetWithImagesResponse> {
        return poetRepository.getPoetWithImages(poetId)
    }

    fun getPoetImages(poetId: Int): ApiResponse<PoetImageResponse> {
        return poetRepository.getPoetImages(poetId)
    }
}
