package dev.mamkin.lazypizza.order.presentation.cart

import dev.mamkin.lazypizza.order.domain.models.MenuItem
import dev.mamkin.lazypizza.order.domain.models.ProductType
import dev.mamkin.lazypizza.order.presentation.models.ProductCardUi
import dev.mamkin.lazypizza.order.presentation.models.toProductType
import dev.mamkin.lazypizza.order.presentation.utils.formatPrice


sealed interface CartState {
    data object Empty : CartState
    data object Loading : CartState
    data class Content(
        val items: List<ProductCardUi>,
        val recommended: List<RecommendedItemUi>,
        val buttonText: String
    ) : CartState
}

data class RecommendedItemUi(
    val id: String,
    val title: String,
    val price: String,
    val image: String,
    val type: ProductType,
)

fun MenuItem.toRecommendedItemUi() = RecommendedItemUi(
    id = id,
    title = title,
    price = formatPrice(price),
    image = image,
    type = toProductType()
)
