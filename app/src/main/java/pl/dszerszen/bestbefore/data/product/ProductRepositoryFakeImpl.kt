package pl.dszerszen.bestbefore.data.product

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pl.dszerszen.bestbefore.domain.product.ProductRepository
import pl.dszerszen.bestbefore.domain.product.model.Product
import pl.dszerszen.bestbefore.util.DispatchersProvider
import pl.dszerszen.bestbefore.util.Response
import pl.dszerszen.bestbefore.util.asSuccess
import java.time.LocalDate
import javax.inject.Inject

class ProductRepositoryFakeImpl @Inject constructor(
    private val dispatchersProvider: DispatchersProvider
) : ProductRepository {
    override suspend fun deleteProduct(product: Product) {
        //do nothing
    }

    override suspend fun addProducts(products: List<Product>) {
        //do nothing
    }
    override fun getAllProducts(): Flow<Response<List<Product>>> = flow {
        delay(1000L)
        emit(List(50){ Product("Product $it", quantity = it + 1, date = LocalDate.parse("2020-01-01")) }.asSuccess())
    }
}