package pl.dszerszen.bestbefore.domain.product.interactor

import kotlinx.coroutines.flow.Flow
import pl.dszerszen.bestbefore.domain.product.ProductRepository
import pl.dszerszen.bestbefore.domain.product.model.Product
import pl.dszerszen.bestbefore.util.DispatchersProvider
import pl.dszerszen.bestbefore.util.Response
import javax.inject.Inject

class GetAllProductsUseCase @Inject constructor(
    private val dispatchersProvider: DispatchersProvider,
    private val repository: ProductRepository
) {
    suspend operator fun invoke(): Flow<Response<List<Product>>> {
        return repository.getAllProducts()
    }
}