package pl.dszerszen.bestbefore.data.product

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import pl.dszerszen.bestbefore.data.product.local.ProductsDatabase
import pl.dszerszen.bestbefore.domain.product.ProductRepository
import pl.dszerszen.bestbefore.domain.product.mapper.toDomain
import pl.dszerszen.bestbefore.domain.product.mapper.toEntity
import pl.dszerszen.bestbefore.domain.product.model.Category
import pl.dszerszen.bestbefore.domain.product.model.Product
import pl.dszerszen.bestbefore.util.Response
import pl.dszerszen.bestbefore.util.asSuccess
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val db: ProductsDatabase
) : ProductRepository {

    override suspend fun deleteProduct(product: Product) {
        db.productsDao.deleteProduct(product.id)
    }

    override suspend fun addProducts(products: List<Product>) {
        db.productsDao.addProducts(products.map { it.toEntity() })
    }

    override fun getAllProducts(): Flow<Response<List<Product>>> {
        return combine(
            db.productsDao.getAllProducts(),
            db.categoriesDao.getCategories()
        ) { products, categories ->
            products.map { productEntity ->
                productEntity.toDomain { categoryId -> categories.find { it.id == categoryId }?.toDomain() }
            }.asSuccess()
        }
    }

    override fun getCategories(): Flow<Response<List<Category>>> {
        return db.categoriesDao.getCategories().map { it.map { category -> category.toDomain() }.asSuccess() }
    }

    override suspend fun addCategory(category: Category) {
        db.categoriesDao.addCategory(category.toEntity())
    }

    override suspend fun deleteCategory(category: Category) {
        db.categoriesDao.deleteCategory(category.toEntity())
    }

}