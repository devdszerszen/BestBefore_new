package pl.dszerszen.bestbefore.domain.product

import kotlinx.coroutines.flow.Flow
import pl.dszerszen.bestbefore.domain.product.model.Product
import pl.dszerszen.bestbefore.util.Response

interface ProductRepository {
    suspend fun deleteProduct(product: Product)
    suspend fun addProducts(products: List<Product>)
    suspend fun getAllProducts(): Flow<Response<List<Product>>>
}