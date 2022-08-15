package pl.dszerszen.bestbefore.data.product.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ProductEntity::class, CategoryEntity::class],
    version = 1
)
abstract class ProductsDatabase : RoomDatabase() {

    abstract val productsDao: ProductsDao
    abstract val categoriesDao: CategoryDao
}