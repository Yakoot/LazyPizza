package dev.mamkin.lazypizza.order.presentation.cart

import dev.mamkin.lazypizza.order.domain.models.ProductType
import dev.mamkin.lazypizza.order.presentation.models.ProductCardUi


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
    val type: ProductType
)