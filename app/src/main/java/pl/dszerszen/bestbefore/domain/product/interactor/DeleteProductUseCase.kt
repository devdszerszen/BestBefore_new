package pl.dszerszen.bestbefore.domain.product.interactor

import pl.dszerszen.bestbefore.domain.product.ProductRepository
import pl.dszerszen.bestbefore.domain.product.model.Product
import javax.inject.Inject

class DeleteProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(product: Product) {
        productRepository.deleteProduct(product)
    }
}