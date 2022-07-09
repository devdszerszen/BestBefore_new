package pl.dszerszen.bestbefore.domain.product

import pl.dszerszen.bestbefore.domain.product.model.Product
import pl.dszerszen.bestbefore.util.Response

interface ProductRepository {
    suspend fun getAllProducts(): Response<List<Product>>
}