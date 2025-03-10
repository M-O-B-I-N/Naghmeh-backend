package mobin.shabanifar.service

import mobin.shabanifar.models.common.PaginatedResponse
import mobin.shabanifar.models.poem.PoemsOfCategoryResponse
import mobin.shabanifar.repository.PoemRepository

class PoemService(private val repository: PoemRepository) {
    fun getPoemsOfCategory(
        poetName: String,
        categoryName: String,
        page: Int,
        pageSize: Int
    ): PaginatedResponse<PoemsOfCategoryResponse> {
        return repository.getPoemsOfCategory(
            poetName = poetName,
            categoryName = categoryName,
            page = page,
            pageSize = pageSize
        )
    }

}
