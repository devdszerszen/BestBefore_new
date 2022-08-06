package pl.dszerszen.bestbefore.data.product.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProducts(product: List<ProductEntity>)

    @Query("SELECT * FROM productentity")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("DELETE FROM productentity WHERE id = :id")
    suspend fun deleteProduct(id: String)
}