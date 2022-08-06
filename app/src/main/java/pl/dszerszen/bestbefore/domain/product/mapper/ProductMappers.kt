package pl.dszerszen.bestbefore.domain.product.mapper

import pl.dszerszen.bestbefore.data.product.local.ProductEntity
import pl.dszerszen.bestbefore.domain.product.model.Product
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun ProductEntity.toDomain(): Product {
    return Product(
        name = name,
        desc = desc,
        quantity = quantity,
        date = LocalDate.parse(date),
        id = id
    )
}

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        name = name,
        desc = desc,
        quantity = quantity,
        date = date.format(DateTimeFormatter.ISO_LOCAL_DATE),
        id = id
    )
}