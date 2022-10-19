package pl.dszerszen.bestbefore.data.product.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    val desc: String?,
    val quantity: Int,
    val date: String,
    val categoriesId: List<String>
)
