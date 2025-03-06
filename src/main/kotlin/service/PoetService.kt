package mobin.shabanifar.service

import mobin.shabanifar.models.poet.Category
import mobin.shabanifar.models.poet.FamousPoet
import mobin.shabanifar.models.poet.PoetWithBirthYear
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

}
