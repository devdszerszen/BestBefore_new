package pl.dszerszen.bestbefore.data.product

import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import pl.dszerszen.bestbefore.domain.product.model.Product
import pl.dszerszen.bestbefore.domain.product.ProductRepository
import pl.dszerszen.bestbefore.util.DispatchersProvider
import pl.dszerszen.bestbefore.util.asSuccess
import javax.inject.Inject

class ProductRepositoryFakeImpl @Inject constructor(
    private val dispatchersProvider: DispatchersProvider
) : ProductRepository {
    override suspend fun getAllProducts() = withContext(dispatchersProvider.ioDispatcher()) {
        delay(1000L)
        List(50){ Product("Product $it", quantity = it + 1) }.asSuccess()
    }
}