package pl.dszerszen.bestbefore.domain.product.model

data class Product(
    val name: String,
    val desc: String? = null,
    val quantity: Int = 1
)