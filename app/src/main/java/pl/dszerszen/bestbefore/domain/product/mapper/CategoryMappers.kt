package pl.dszerszen.bestbefore.domain.product.mapper

import pl.dszerszen.bestbefore.data.product.local.CategoryEntity
import pl.dszerszen.bestbefore.domain.product.model.Category
import pl.dszerszen.bestbefore.domain.product.model.parseCategoryIconOrNull

fun CategoryEntity.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        icon = iconName?.parseCategoryIconOrNull()
    )
}

fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        iconName = icon?.name
    )
}