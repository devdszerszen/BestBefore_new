package pl.dszerszen.bestbefore.domain.product.interactor

import pl.dszerszen.bestbefore.domain.product.ProductRepository
import pl.dszerszen.bestbefore.domain.product.model.Product
import javax.inject.Inject

class AddProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(products: List<Product>) {
        repository.addProducts(products)
    }
}