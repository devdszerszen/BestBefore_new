package pl.dszerszen.bestbefore.data.product

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.dszerszen.bestbefore.data.product.local.ProductsDatabase
import pl.dszerszen.bestbefore.domain.product.ProductRepository
import pl.dszerszen.bestbefore.domain.product.mapper.toDomain
import pl.dszerszen.bestbefore.domain.product.mapper.toEntity
import pl.dszerszen.bestbefore.domain.product.model.Product
import pl.dszerszen.bestbefore.util.Response
import pl.dszerszen.bestbefore.util.asSuccess
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val db: ProductsDatabase
) : ProductRepository {

    override suspend fun deleteProduct(product: Product) {
        db.dao.deleteProduct(product.id)
    }

    override suspend fun addProducts(products: List<Product>) {
        db.dao.addProducts(products.map { it.toEntity() })
    }

    override suspend fun getAllProducts(): Flow<Response<List<Product>>> {
        return db.dao.getAllProducts().map { it.map { product -> product.toDomain() }.asSuccess() }
    }

}