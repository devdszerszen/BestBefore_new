package pl.dszerszen.bestbefore.data.product.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    val name: String,
    val desc: String?,
    val quantity: Int,
    val date: String,
    @PrimaryKey val id: String
)
