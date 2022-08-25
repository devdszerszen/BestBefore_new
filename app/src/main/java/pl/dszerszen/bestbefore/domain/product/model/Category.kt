package pl.dszerszen.bestbefore.domain.product.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

data class Category(
    val id: String,
    val name: String,
    val icon: CategoryIcon? = null,
    val selected: Boolean = false,
)

enum class CategoryIcon(val icon: ImageVector) {
    DEFAULT(Icons.Default.Star)
}

fun String.parseCategoryIconOrNull(): CategoryIcon? {
    return CategoryIcon.values().firstOrNull{ it.name == this }
}
