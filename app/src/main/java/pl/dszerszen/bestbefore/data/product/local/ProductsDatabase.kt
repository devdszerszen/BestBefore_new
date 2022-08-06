package pl.dszerszen.bestbefore.data.product.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ProductEntity::class],
    version = 1
)
abstract class ProductsDatabase : RoomDatabase() {

    abstract val dao: ProductsDao
}