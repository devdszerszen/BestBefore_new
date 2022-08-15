package pl.dszerszen.bestbefore.domain.product.interactor

import kotlinx.coroutines.flow.Flow
import pl.dszerszen.bestbefore.domain.product.ProductRepository
import pl.dszerszen.bestbefore.domain.product.model.Category
import pl.dszerszen.bestbefore.util.Response
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val productsRepository: ProductRepository
) {
    operator fun invoke(): Flow<Response<List<Category>>> {
        return productsRepository.getCategories()
    }
}