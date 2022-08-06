package pl.dszerszen.bestbefore.domain.product.model

import java.time.LocalDate
import java.util.*

data class Product(
    val name: String,
    val desc: String? = null,
    val quantity: Int = 1,
    val date: LocalDate,
    val id: String = UUID.randomUUID().toString()
)