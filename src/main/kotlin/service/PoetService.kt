package mobin.shabanifar.service

import mobin.shabanifar.models.poet.*
import mobin.shabanifar.repository.PoetRepository


class PoetService(private val poetRepository: PoetRepository) {

    fun getPoetsByCentury(century: Int): List<PoetWithBirthYear?> {
        return poetRepository.getPoetsByCentury(century)
    }

    fun getWorksOfPoet(poetName: String): List<Category> {
        return poetRepository.getWorksOfPoet(poetName)
    }

    fun getTop8FamousPoets(): List<FamousPoet>{
        return poetRepository.getTop8FamousPoets()
    }

    fun getPoetWithImages(poetId: Int) : PoetWithImagesResponse? {
        return poetRepository.getPoetWithImages(poetId)
    }

    fun getPoetImages(poetId: Int) : PoetImageResponse {
        return poetRepository.getPoetImages(poetId)
    }

}
