package pl.dszerszen.bestbefore.data.product.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [ProductEntity::class, CategoryEntity::class],
    version = 1
)
@TypeConverters(StringListConverter::class)
abstract class ProductsDatabase : RoomDatabase() {

    abstract val productsDao: ProductsDao
    abstract val categoriesDao: CategoryDao
}