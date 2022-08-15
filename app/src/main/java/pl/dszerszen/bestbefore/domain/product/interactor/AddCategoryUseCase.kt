package pl.dszerszen.bestbefore.domain.product.interactor

import pl.dszerszen.bestbefore.domain.product.ProductRepository
import pl.dszerszen.bestbefore.domain.product.model.Category
import javax.inject.Inject

class AddCategoryUseCase @Inject constructor(
    private val productsRepository: ProductRepository
) {
    suspend operator fun invoke(category: Category) {
        productsRepository.addCategory(category)
    }
}